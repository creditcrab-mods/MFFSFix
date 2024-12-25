package mffs.tileentity;

import java.util.HashSet;
import java.util.Set;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mffs.ModularForceFieldSystem;
import mffs.Settings;
import mffs.api.ICache;
import mffs.api.IProjector;
import mffs.api.modules.IModule;
import mffs.api.modules.IProjectorMode;
import mffs.block.BlockForceField;
import mffs.card.ItemCard;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import universalelectricity.core.vector.Vector3;

public class TileEntityForceFieldProjector extends TileEntityFieldInteraction
    implements IProjector, ProjectorCalculationThread.IThreadCallBack {
    protected final Set<Vector3> forceFields;

    public TileEntityForceFieldProjector() {
        this.forceFields = new HashSet<>();
        super.capacityBase = 50;
        super.startModuleIndex = 1;
    }

    @Override
    public void initiate() {
        super.initiate();
        this.calculateForceField();
    }

    @Override
    public void onFxsPacket(NBTTagCompound data) {
        Vector3 otherPos = Vector3.readFromNBT(data);
        int type = data.getInteger("type");

        Vector3 vector = otherPos.add(0.5);
        final Vector3 root = new Vector3(this).add(0.5);
        if (type == 1) {
            ModularForceFieldSystem.proxy.renderBeam(
                this.getWorldObj(), root, vector, 0.6f, 0.6f, 1.0f, 40
            );
            ModularForceFieldSystem.proxy.renderHologramMoving(
                this.getWorldObj(), vector, 1.0f, 1.0f, 1.0f, 50
            );
        } else if (type == 2) {
            ModularForceFieldSystem.proxy.renderBeam(
                this.getWorldObj(), vector, root, 1.0f, 0.0f, 0.0f, 40
            );
            ModularForceFieldSystem.proxy.renderHologramMoving(
                this.getWorldObj(), vector, 1.0f, 0.0f, 0.0f, 50
            );
        }
    }

    @Override
    protected void
    calculateForceField(final ProjectorCalculationThread.IThreadCallBack callBack) {
        if (!this.getWorldObj().isRemote && !super.isCalculating
            && this.getMode() != null) {
            this.forceFields.clear();
        }
        super.calculateForceField(callBack);
    }

    @Override
    public void onThreadComplete() {
        this.destroyField();
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (this.isActive() && this.getMode() != null
            && this.requestFortron(this.getFortronCost(), false)
                >= this.getFortronCost()) {
            this.consumeCost();
            if (!this.getWorldObj().isRemote) {
                if (super.ticks % 10L == 0L) {
                    if (!super.isCalculated) {
                        this.calculateForceField();
                    } else {
                        this.projectField();
                    }
                }
            } else if (this.isActive()) {
                super.animation += this.getFortronCost() / 3;
            }
            if (super.ticks % 40L == 0L
                && this.getModuleCount(
                       ModularForceFieldSystem.itemModuleSilence, new int[0]
                   ) <= 0) {
                this.getWorldObj().playSoundEffect(
                    this.xCoord + 0.5,
                    this.yCoord + 0.5,
                    this.zCoord + 0.5,
                    "mffs:field",
                    0.6f,
                    1.0f - this.getWorldObj().rand.nextFloat() * 0.1f
                );
            }
        } else if (!this.getWorldObj().isRemote) {
            this.destroyField();
        }
    }

    @Override
    public int getFortronCost() {
        return super.getFortronCost() + 5;
    }

    @Override
    public float getAmplifier() {
        return (float) Math.max(Math.min(this.getCalculatedField().size() / 1000, 10), 1);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        this.destroyField();
    }

    @Override
    public void projectField() {
    Label_0636: {
        if (!this.getWorldObj().isRemote && super.isCalculated && !super.isCalculating) {
            if (this.forceFields.size() <= 0
                && this.getModeStack().getItem() instanceof ICache) {
                ((ICache) this.getModeStack().getItem()).clearCache();
            }
            int constructionCount = 0;
            final int constructionSpeed
                = Math.min(this.getProjectionSpeed(), Settings.MAX_FORCE_FIELDS_PER_TICK);
            final HashSet<Vector3> fieldToBeProjected = new HashSet<>();
            fieldToBeProjected.addAll(super.calculatedField);
            for (final IModule module : this.getModules(this.getModuleSlots())) {
                if (module.onProject(this, fieldToBeProjected)) {
                    return;
                }
            }
        Label_0158:
            for (final Vector3 vector : super.calculatedField) {
                if (fieldToBeProjected.contains(vector)) {
                    if (constructionCount > constructionSpeed) {
                        break;
                    }
                    final Block block
                        = vector.getBlock((IBlockAccess) this.getWorldObj());
                    if ((block != null
                         && (this.getModuleCount(
                                 ModularForceFieldSystem.itemModuleDisintegration,
                                 new int[0]
                             ) <= 0
                             || block.getBlockHardness(
                                    this.getWorldObj(),
                                    vector.intX(),
                                    vector.intY(),
                                    vector.intZ()
                                ) == -1.0f)
                         && !block.getMaterial().isLiquid() && block != Blocks.snow
                         && block != Blocks.vine && block != Blocks.tallgrass
                         && block != Blocks.deadbush
                         && !block.isReplaceable(
                             this.getWorldObj(),
                             vector.intX(),
                             vector.intY(),
                             vector.intZ()
                         ))
                        || block == ModularForceFieldSystem.blockForceField
                        || vector.equals(new Vector3(this))
                        || !this.getWorldObj()
                                .getChunkFromBlockCoords(vector.intX(), vector.intZ())
                                .isChunkLoaded) {
                        continue;
                    }
                    for (final IModule module2 : this.getModules(this.getModuleSlots())) {
                        final int flag = module2.onProject(this, vector.clone());
                        if (flag == 1) {
                            continue Label_0158;
                        }
                        if (flag == 2) {
                            break Label_0636;
                        }
                    }
                    this.getWorldObj().setBlock(
                        vector.intX(),
                        vector.intY(),
                        vector.intZ(),
                        ModularForceFieldSystem.blockForceField,
                        0,
                        2
                    );
                    final TileEntity tileEntity = this.getWorldObj().getTileEntity(
                        vector.intX(), vector.intY(), vector.intZ()
                    );
                    if (tileEntity instanceof TileEntityForceField) {
                        ((TileEntityForceField) tileEntity)
                            .setProjector(new Vector3(this));
                    }
                    this.requestFortron(1, true);
                    this.forceFields.add(vector);
                    ++constructionCount;
                } else {
                    final Block block
                        = vector.getBlock((IBlockAccess) this.getWorldObj());
                    if (block != ModularForceFieldSystem.blockForceField
                        || ((BlockForceField) block)
                                .getProjector(
                                    (IBlockAccess) this.getWorldObj(),
                                    vector.intX(),
                                    vector.intY(),
                                    vector.intZ()
                                )
                            != this) {
                        continue;
                    }
                    this.getWorldObj().setBlockToAir(
                        vector.intX(), vector.intY(), vector.intZ()
                    );
                }
            }
        }
    }
    }

    @Override
    public void destroyField() {
        if (!this.getWorldObj().isRemote && super.isCalculated && !super.isCalculating) {
            final HashSet<Vector3> copiedSet = new HashSet<>();
            copiedSet.addAll(super.calculatedField);
            for (final Vector3 vector : copiedSet) {
                final Block block = vector.getBlock((IBlockAccess) this.getWorldObj());
                if (block == ModularForceFieldSystem.blockForceField) {
                    this.getWorldObj().setBlock(
                        vector.intX(), vector.intY(), vector.intZ(), Blocks.air, 0, 3
                    );
                }
            }
        }
        this.forceFields.clear();
        super.calculatedField.clear();
        super.isCalculated = false;
    }

    @Override
    public void invalidate() {
        this.destroyField();
        super.invalidate();
    }

    @Override
    public int getProjectionSpeed() {
        return 28
            + 28
            * this.getModuleCount(
                ModularForceFieldSystem.itemModuleSpeed, this.getModuleSlots()
            );
    }

    @Override
    public int getSizeInventory() {
        return 21;
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
    public Set<ItemStack> getCards() {
        final Set<ItemStack> cards = new HashSet<>();
        cards.add(super.getCard());
        cards.add(this.getStackInSlot(1));
        return cards;
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return AxisAlignedBB.getBoundingBox(
            (double) this.xCoord,
            (double) this.yCoord,
            (double) this.zCoord,
            (double) (this.xCoord + 1),
            (double) (this.yCoord + 2),
            (double) (this.zCoord + 1)
        );
    }

    @Override
    public long getTicks() {
        return super.ticks;
    }
}
