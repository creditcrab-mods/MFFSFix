package mffs.tileentity;

import java.util.List;
import java.util.Set;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import mffs.ModularForceFieldSystem;
import mffs.Settings;
import mffs.api.Blacklist;
import mffs.api.ISpecialForceManipulation;
import mffs.api.modules.IModule;
import mffs.api.modules.IProjectorMode;
import mffs.base.PacketFxs;
import mffs.base.PacketTile;
import mffs.card.ItemCard;
import mffs.event.BlockPreMoveDelayedEvent;
import mffs.fortron.FortronHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import universalelectricity.core.vector.Vector3;

public class TileEntityForceManipulator extends TileEntityFieldInteraction {
    public static final int ANIMATION_TIME = 20;
    public Vector3 anchor;
    public int displayMode;
    public boolean isCalculatingManipulation;
    public Set<Vector3> manipulationVectors;
    public boolean doAnchor;

    public TileEntityForceManipulator() {
        this.anchor = null;
        this.displayMode = 1;
        this.isCalculatingManipulation = false;
        this.manipulationVectors = null;
        this.doAnchor = true;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (this.anchor == null) {
            this.anchor = new Vector3();
        }
        if (this.getMode() != null && Settings.ENABLE_MANIPULATOR) {
            if (!this.getWorldObj().isRemote && this.manipulationVectors != null
                && !this.isCalculatingManipulation) {
                final ForgeDirection dir = this.getDirection(
                    (IBlockAccess) this.getWorldObj(),
                    this.xCoord,
                    this.yCoord,
                    this.zCoord
                );
                final NBTTagCompound nbt = new NBTTagCompound();
                final NBTTagList nbtList = new NBTTagList();
                int i = 0;
                for (final Vector3 position : this.manipulationVectors) {
                    if (this.moveBlock(position, dir) && this.isBlockVisible(position)
                        && i < Settings.MAX_FORCE_FIELDS_PER_TICK) {
                        nbtList.appendTag((NBTBase
                        ) position.writeToNBT(new NBTTagCompound()));
                        ++i;
                    }
                }
                nbt.setByte("type", (byte) 2);
                nbt.setTag("list", (NBTBase) nbtList);
                ModularForceFieldSystem.channel.sendToAll(
                    new PacketFxs(new Vector3(this), nbt)
                );
                if (this.doAnchor) {
                    this.anchor = this.anchor.modifyPositionFromSide(dir);
                }
                this.updatePushedObjects(0.022f);
                this.manipulationVectors = null;
                this.markDirty();
            }
            if (this.isActive() && super.ticks % 20L == 0L
                && this.requestFortron(this.getFortronCost(), false) > 0) {
                if (!this.getWorldObj().isRemote) {
                    this.requestFortron(this.getFortronCost(), true);
                    new ManipulatorCalculationThread(this).start();
                }
                if (this.getModuleCount(
                        ModularForceFieldSystem.itemModuleSilence, new int[0]
                    )
                    <= 0) {
                    this.getWorldObj().playSoundEffect(
                        this.xCoord + 0.5,
                        this.yCoord + 0.5,
                        this.zCoord + 0.5,
                        "mffs:fieldmove",
                        0.6f,
                        1.0f - this.getWorldObj().rand.nextFloat() * 0.1f
                    );
                }
                this.setActive(false);
            }
            if (!this.getWorldObj().isRemote) {
                if (!super.isCalculated) {
                    this.calculateForceField();
                }
                if (super.ticks % 120L == 0L && !super.isCalculating
                    && Settings.HIGH_GRAPHICS && this.getDelayedEvents().size() <= 0
                    && this.displayMode > 0) {
                    final NBTTagCompound nbt2 = new NBTTagCompound();
                    final NBTTagList nbtList2 = new NBTTagList();
                    int j = 0;
                    for (final Vector3 position2 : this.getInteriorPoints()) {
                        if (this.isBlockVisible(position2)
                            && (this.displayMode == 2
                                || position2.getBlock((IBlockAccess) this.getWorldObj())
                                    != Blocks.air)
                            && j < Settings.MAX_FORCE_FIELDS_PER_TICK) {
                            nbtList2.appendTag((NBTBase
                            ) position2.writeToNBT(new NBTTagCompound()));
                            ++j;
                        }
                    }
                    nbt2.setByte("type", (byte) 1);
                    nbt2.setTag("list", (NBTBase) nbtList2);

                    ModularForceFieldSystem.channel.sendToAll(
                        new PacketFxs(new Vector3(this), nbt2)
                    );
                }
            }
        }
    }

    public boolean isBlockVisible(final Vector3 position) {
        int i = 0;
        for (final ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
            final Vector3 checkPos = position.clone().modifyPositionFromSide(direction);
            final Block block = checkPos.getBlock((IBlockAccess) this.getWorldObj());
            if (block != Blocks.air && block.isOpaqueCube()) {
                ++i;
            }
        }
        return i < ForgeDirection.VALID_DIRECTIONS.length;
    }

    @Override
    public void onFxsPacket(NBTTagCompound nbt) {
        final byte type = nbt.getByte("type");
        final NBTTagList nbtList = (NBTTagList) nbt.getTag("list");
        for (int i = 0; i < nbtList.tagCount(); ++i) {
            final Vector3 vector
                = Vector3.readFromNBT(nbtList.getCompoundTagAt(i)).add(0.5);
            if (type == 1) {
                ModularForceFieldSystem.proxy.renderHologram(
                    this.getWorldObj(),
                    vector,
                    1.0f,
                    1.0f,
                    1.0f,
                    30,
                    vector.clone().modifyPositionFromSide(this.getDirection(
                        (IBlockAccess) this.getWorldObj(),
                        this.xCoord,
                        this.yCoord,
                        this.zCoord
                    ))
                );
            } else if (type == 2) {
                ModularForceFieldSystem.proxy.renderHologram(
                    this.getWorldObj(),
                    vector,
                    1.0f,
                    0.0f,
                    0.0f,
                    30,
                    vector.clone().modifyPositionFromSide(this.getDirection(
                        (IBlockAccess) this.getWorldObj(),
                        this.xCoord,
                        this.yCoord,
                        this.zCoord
                    ))
                );
                this.updatePushedObjects(0.022f);
            }
        }
    }

    @Override
    public void onReceivePacket(PacketTile.Type type, final NBTTagCompound dataStream) {
        super.onReceivePacket(type, dataStream);
        if (type == PacketTile.Type.TOGGLE_MODE && !this.getWorldObj().isRemote) {
            switch (dataStream.getInteger("buttonId")) {
                case 1:
                    this.anchor = null;
                    this.markDirty();
                    break;

                case 2:
                    this.displayMode = (this.displayMode + 1) % 3;
                    break;

                case 3:
                    this.doAnchor = !this.doAnchor;
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public int getFortronCost() {
        return (int) ((super.getFortronCost() + this.anchor.getMagnitude()) * 1000.0);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        super.isCalculated = false;
    }

    protected boolean canMove() {
        final Set<Vector3> mobilizationPoints = this.getInteriorPoints();
        final ForgeDirection dir = this.getDirection(
            (IBlockAccess) this.getWorldObj(), this.xCoord, this.yCoord, this.zCoord
        );
    Label_0033:
        for (final Vector3 position : mobilizationPoints) {
            if (position.getBlock((IBlockAccess) this.getWorldObj()) != Blocks.air) {
                if (Blacklist.forceManipulationBlacklist.contains(
                        position.getBlock((IBlockAccess) this.getWorldObj())
                    )) {
                    return false;
                }
                final TileEntity tileEntity
                    = position.getTileEntity((IBlockAccess) this.getWorldObj());
                if (tileEntity instanceof ISpecialForceManipulation
                    && !((ISpecialForceManipulation) tileEntity)
                            .preMove(position.intX(), position.intY(), position.intZ())) {
                    return false;
                }
                final Vector3 targetPosition
                    = position.clone().modifyPositionFromSide(dir);
                if (targetPosition.getTileEntity((IBlockAccess) this.getWorldObj())
                    == this) {
                    return false;
                }
                for (final Vector3 checkPos : mobilizationPoints) {
                    if (checkPos.equals(targetPosition)) {
                        continue Label_0033;
                    }
                }
                final Block blockID
                    = targetPosition.getBlock((IBlockAccess) this.getWorldObj());
                if (blockID != Blocks.air
                    && (!blockID.isReplaceable(
                            this.getWorldObj(),
                            targetPosition.intX(),
                            targetPosition.intY(),
                            targetPosition.intZ()
                        )
                        && !(blockID instanceof BlockLiquid))) {
                    return false;
                }
                continue;
            }
        }
        return true;
    }

    protected boolean moveBlock(final Vector3 position, final ForgeDirection direction) {
        if (!this.getWorldObj().isRemote) {
            final Vector3 newPosition
                = position.clone().modifyPositionFromSide(direction);
            final TileEntity tileEntity
                = position.getTileEntity((IBlockAccess) this.getWorldObj());
            final Block blockID = position.getBlock((IBlockAccess) this.getWorldObj());
            if (blockID != Blocks.air && tileEntity != this) {
                this.getDelayedEvents().add(new BlockPreMoveDelayedEvent(
                    this, 20, this.getWorldObj(), position, newPosition
                ));
                return true;
            }
        }
        return false;
    }

    public void updatePushedObjects(final float amount) {
        final ForgeDirection dir = this.getDirection(
            (IBlockAccess) this.getWorldObj(), this.xCoord, this.yCoord, this.zCoord
        );
        final AxisAlignedBB axisalignedbb = this.getSearchAxisAlignedBB();
        if (axisalignedbb != null) {
            final List<Entity> entities
                = this.getWorldObj().getEntitiesWithinAABB(Entity.class, axisalignedbb);
            for (final Entity entity : entities) {
                entity.addVelocity(
                    (double) (amount * dir.offsetX),
                    (double) (amount * dir.offsetY),
                    (double) (amount * dir.offsetZ)
                );
            }
        }
    }

    public AxisAlignedBB getSearchAxisAlignedBB() {
        final Vector3 positiveScale
            = new Vector3(this).add(this.getTranslation()).add(this.getPositiveScale());
        final Vector3 negativeScale = new Vector3(this)
                                          .add(this.getTranslation())
                                          .subtract(this.getNegativeScale());
        final Vector3 minScale = new Vector3(
            Math.min(positiveScale.x, negativeScale.x),
            Math.min(positiveScale.y, negativeScale.y),
            Math.min(positiveScale.z, negativeScale.z)
        );
        final Vector3 maxScale = new Vector3(
            Math.max(positiveScale.x, negativeScale.x),
            Math.max(positiveScale.y, negativeScale.y),
            Math.max(positiveScale.z, negativeScale.z)
        );
        return AxisAlignedBB.getBoundingBox(
            (double) minScale.intX(),
            (double) minScale.intY(),
            (double) minScale.intZ(),
            (double) maxScale.intX(),
            (double) maxScale.intY(),
            (double) maxScale.intZ()
        );
    }

    @Override
    public boolean isItemValidForSlot(final int slotID, final ItemStack itemStack) {
        if (slotID == 0 || slotID == 1) {
            return itemStack.getItem() instanceof ItemCard;
        }
        if (slotID == 2) {
            return itemStack.getItem() instanceof IProjectorMode;
        }
        return slotID >= 15 || itemStack.getItem() instanceof IModule;
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.anchor = Vector3.readFromNBT(nbt.getCompoundTag("anchor"));
        this.displayMode = nbt.getInteger("displayMode");
        this.doAnchor = nbt.getBoolean("doAnchor");
    }

    @Override
    public void writeToNBT(final NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        if (this.anchor != null) {
            nbt.setTag("anchor", this.anchor.writeToNBT(new NBTTagCompound()));
        }
        nbt.setInteger("displayMode", this.displayMode);
        nbt.setBoolean("doAnchor", this.doAnchor);
    }

    @Override
    public Vector3 getTranslation() {
        return super.getTranslation().clone().add(this.anchor);
    }

    @Override
    public int getSizeInventory() {
        return 21;
    }

    @Override
    public String[] getMethodNames() {
        return new String[] { "isActivate", "setActivate", "resetAnchor" };
    }

    @Override
    public Object[] callMethod(
        IComputerAccess computer, ILuaContext context, int method, Object[] arguments
    ) throws LuaException, InterruptedException {
        switch (method) {
            case 0:
            case 1:
                return super.callMethod(computer, context, method, arguments);

            case 2:
                this.anchor = null;
                return null;

            default:
                return null;
        }
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setBoolean("isActive", this.isActive);
        nbt.setInteger("fortron", this.fortronTank.getFluidAmount());
        if (this.anchor != null) {
            nbt.setTag("anchor", this.anchor.writeToNBT(new NBTTagCompound()));
        }
        nbt.setInteger("displayMode", this.displayMode);
        nbt.setBoolean("doAnchor", this.doAnchor);

        return new S35PacketUpdateTileEntity(
            this.xCoord, this.yCoord, this.zCoord, this.getBlockMetadata(), nbt
        );
    }

    @Override
    public void onDataPacket(NetworkManager arg0, S35PacketUpdateTileEntity arg1) {
        NBTTagCompound nbt = arg1.func_148857_g();

        this.isActive = nbt.getBoolean("isActive");
        this.fortronTank.setFluid(
            new FluidStack(FortronHelper.FLUID_FORTRON, nbt.getInteger("fortron"))
        );
        if (nbt.hasKey("anchor")) {
            this.anchor = Vector3.readFromNBT(nbt.getCompoundTag("anchor"));
        }
        this.displayMode = nbt.getInteger("displayMode");
        this.doAnchor = nbt.getBoolean("doAnchor");
    }
}
