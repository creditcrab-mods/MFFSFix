package mffs.tileentity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mffs.ModularForceFieldSystem;
import mffs.Settings;
import mffs.api.modules.IInterdictionMatrixModule;
import mffs.api.modules.IModule;
import mffs.api.security.IBiometricIdentifier;
import mffs.api.security.IInterdictionMatrix;
import mffs.api.security.Permission;
import mffs.base.PacketTile;
import mffs.base.TileEntityModuleAcceptor;
import mffs.card.ItemCard;
import mffs.fortron.FortronHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fluids.FluidStack;

public class TileEntityInterdictionMatrix
    extends TileEntityModuleAcceptor implements IInterdictionMatrix {
    private boolean isBanMode;

    public TileEntityInterdictionMatrix() {
        this.isBanMode = true;
        super.capacityBase = 30;
        super.startModuleIndex = 2;
        super.endModuleIndex = 9;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (!this.getWorldObj().isRemote
            && (this.isActive()
                || (this.getStackInSlot(0) != null
                    && this.getStackInSlot(0).getItem()
                        == ModularForceFieldSystem.itemCardInfinite))
            && super.ticks % 10L == 0L
            && this.requestFortron(this.getFortronCost() * 10, false) > 0) {
            this.requestFortron(this.getFortronCost() * 10, true);
            this.scan();
        }
    }

    public float getAmplifier() {
        return (float) Math.max(Math.min(this.getActionRange() / 20, 10), 1);
    }

    public void scan() {
        try {
            final IBiometricIdentifier biometricIdentifier
                = this.getBiometricIdentifier();
            final AxisAlignedBB emptyBounds = AxisAlignedBB.getBoundingBox(
                (double) this.xCoord,
                (double) this.yCoord,
                (double) this.zCoord,
                (double) (this.xCoord + 1),
                (double) (this.yCoord + 1),
                (double) (this.zCoord + 1)
            );
            final List<EntityLivingBase> warningList
                = this.getWorldObj().getEntitiesWithinAABB(
                    EntityLivingBase.class,
                    emptyBounds.expand(
                        (double) this.getWarningRange(),
                        (double) this.getWarningRange(),
                        (double) this.getWarningRange()
                    )
                );
            final List<EntityLivingBase> actionList
                = this.getWorldObj().getEntitiesWithinAABB(
                    EntityLivingBase.class,
                    emptyBounds.expand(
                        (double) this.getActionRange(),
                        (double) this.getActionRange(),
                        (double) this.getActionRange()
                    )
                );
            for (final EntityLivingBase entityLiving : warningList) {
                if (entityLiving instanceof EntityPlayer
                    && !actionList.contains(entityLiving)) {
                    final EntityPlayer player = (EntityPlayer) entityLiving;
                    boolean isGranted = false;
                    if (biometricIdentifier != null
                        && biometricIdentifier.isAccessGranted(
                            player.getDisplayName(), Permission.BYPASS_INTERDICTION_MATRIX
                        )) {
                        isGranted = true;
                    }
                    if (isGranted || this.getWorldObj().rand.nextInt(3) != 0) {
                        continue;
                    }
                    player.addChatMessage(new ChatComponentText(
                        "[" + this.getInventoryName()
                        + "] Warning! You are near the scanning range!"
                    ));
                }
            }
            if (this.getWorldObj().rand.nextInt(3) == 0) {
                for (final EntityLivingBase entityLiving : actionList) {
                    this.applyAction(entityLiving);
                }
            }
        } catch (final Exception e) {
            ModularForceFieldSystem.LOGGER.severe("Defense Station has an error!");
            e.printStackTrace();
        }
    }

    public void applyAction(final EntityLivingBase entityLivingBase) {
        if (entityLivingBase instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer) entityLivingBase;
            final IBiometricIdentifier biometricIdentifier
                = this.getBiometricIdentifier();
            if (biometricIdentifier != null
                && biometricIdentifier.isAccessGranted(
                    player.getDisplayName(), Permission.BYPASS_INTERDICTION_MATRIX
                )) {
                return;
            }
            if (!Settings.INTERACT_CREATIVE && player.capabilities.isCreativeMode) {
                return;
            }
        }
        for (final ItemStack itemStack : this.getModuleStacks(new int[0])) {
            if (itemStack.getItem() instanceof IInterdictionMatrixModule) {
                final IInterdictionMatrixModule module
                    = (IInterdictionMatrixModule) itemStack.getItem();
                if (module.onDefend(this, entityLivingBase)) {
                    break;
                }
                if (((Entity) entityLivingBase).isDead) {
                    break;
                }
                continue;
            }
        }
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setBoolean("isBanMode", this.isBanMode);
        nbt.setBoolean("isActive", this.isActive);
        nbt.setInteger("fortron", this.fortronTank.getFluidAmount());

        return new S35PacketUpdateTileEntity(
            this.xCoord, this.yCoord, this.zCoord, this.getBlockMetadata(), nbt
        );
    }

    @Override
    public void onDataPacket(NetworkManager arg0, S35PacketUpdateTileEntity arg1) {
        NBTTagCompound nbt = arg1.func_148857_g();

        this.isBanMode = nbt.getBoolean("isBanMode");
        this.isActive = nbt.getBoolean("isActive");
        this.fortronTank.setFluid(
            new FluidStack(FortronHelper.FLUID_FORTRON, nbt.getInteger("fortron"))
        );
    }

    @Override
    public void onReceivePacket(PacketTile.Type type, final NBTTagCompound dataStream) {
        super.onReceivePacket(type, dataStream);
        if (type == PacketTile.Type.TOGGLE_MODE) {
            this.isBanMode = !this.isBanMode;
        }
    }

    public boolean isBanMode() {
        return this.isBanMode;
    }

    @Override
    public int getActionRange() {
        return this.getModuleCount(ModularForceFieldSystem.itemModuleScale, new int[0]);
    }

    @Override
    public int getWarningRange() {
        return this.getModuleCount(ModularForceFieldSystem.itemModuleWarn, new int[0])
            + this.getActionRange() + 3;
    }

    @Override
    public int getSizeInventory() {
        return 19;
    }

    @Override
    public Set<ItemStack> getFilteredItems() {
        final Set<ItemStack> stacks = new HashSet<>();
        for (int i = super.endModuleIndex; i < this.getSizeInventory() - 1; ++i) {
            if (this.getStackInSlot(i) != null) {
                stacks.add(this.getStackInSlot(i));
            }
        }
        return stacks;
    }

    @Override
    public boolean getFilterMode() {
        return this.isBanMode;
    }

    @Override
    public boolean isItemValidForSlot(final int slotID, final ItemStack itemStack) {
        if (slotID == 0 || slotID == 1) {
            return itemStack.getItem() instanceof ItemCard;
        }
        return slotID > super.endModuleIndex || itemStack.getItem() instanceof IModule;
    }

    @Override
    public Set<ItemStack> getCards() {
        final Set<ItemStack> cards = new HashSet<>();
        cards.add(super.getCard());
        cards.add(this.getStackInSlot(1));
        return cards;
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.isBanMode = nbt.getBoolean("isBanMode");
    }

    @Override
    public void writeToNBT(final NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setBoolean("isBanMode", this.isBanMode);
    }
}
