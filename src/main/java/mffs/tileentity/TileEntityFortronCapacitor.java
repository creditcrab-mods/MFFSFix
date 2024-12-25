package mffs.tileentity;

import java.util.HashSet;
import java.util.Set;

import icbm.api.IBlockFrequency;
import mffs.MFFSHelper;
import mffs.ModularForceFieldSystem;
import mffs.TransferMode;
import mffs.api.card.ICard;
import mffs.api.card.ICardInfinite;
import mffs.api.card.ICardLink;
import mffs.api.fortron.IFortronCapacitor;
import mffs.api.fortron.IFortronFrequency;
import mffs.api.modules.IModule;
import mffs.base.PacketTile;
import mffs.base.TileEntityModuleAcceptor;
import mffs.fortron.FortronHelper;
import mffs.fortron.FrequencyGrid;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.FluidStack;
import universalelectricity.core.vector.Vector3;

public class TileEntityFortronCapacitor
    extends TileEntityModuleAcceptor implements IFortronCapacitor {
    private TransferMode transferMode;

    public TileEntityFortronCapacitor() {
        this.transferMode = TransferMode.EQUALIZE;
        super.capacityBase = 700;
        super.capacityBoost = 10;
        super.startModuleIndex = 2;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        this.consumeCost();
        if (!this.isDisabled() && this.isActive() && super.ticks % 10L == 0L) {
            Set<IFortronFrequency> machines = new HashSet<>();
            for (final ItemStack itemStack : this.getCards()) {
                if (itemStack != null) {
                    if (itemStack.getItem() instanceof ICardInfinite) {
                        this.setFortronEnergy(this.getFortronCapacity());
                    } else {
                        if (!(itemStack.getItem() instanceof ICardLink)) {
                            continue;
                        }
                        final Vector3 linkPosition
                            = ((ICardLink) itemStack.getItem()).getLink(itemStack);
                        if (linkPosition == null
                            || !(
                                linkPosition.getTileEntity((IBlockAccess
                                ) this.getWorldObj())
                                    instanceof IFortronFrequency
                            )) {
                            continue;
                        }
                        machines.add(this);
                        machines.add((IFortronFrequency
                        ) linkPosition.getTileEntity((IBlockAccess) this.getWorldObj()));
                    }
                }
            }
            if (machines.size() < 1) {
                machines = this.getLinkedDevices();
            }
            MFFSHelper.transferFortron(
                this, machines, this.transferMode, this.getTransmissionRate()
            );
        }
    }

    public float getAmplifier() {
        return 0.001f;
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setInteger("transferMode", this.transferMode.ordinal());
        nbt.setBoolean("isActive", this.isActive);
        nbt.setInteger("fortron", this.fortronTank.getFluidAmount());

        return new S35PacketUpdateTileEntity(
            this.xCoord, this.yCoord, this.zCoord, this.getBlockMetadata(), nbt
        );
    }

    @Override
    public void onDataPacket(NetworkManager arg0, S35PacketUpdateTileEntity arg1) {
        NBTTagCompound nbt = arg1.func_148857_g();

        this.transferMode = TransferMode.values()[nbt.getInteger("transferMode")];
        this.isActive = nbt.getBoolean("isActive");
        this.fortronTank.setFluid(
            new FluidStack(FortronHelper.FLUID_FORTRON, nbt.getInteger("fortron"))
        );
    }

    @Override
    public void onReceivePacket(PacketTile.Type type, final NBTTagCompound dataStream) {
        super.onReceivePacket(type, dataStream);
        if (type == PacketTile.Type.TOGGLE_MODE) {
            this.transferMode = this.transferMode.toggle();
        }
    }

    public int getSizeInventory() {
        return 5;
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.transferMode = TransferMode.values()[nbt.getInteger("transferMode")];
    }

    @Override
    public void writeToNBT(final NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("transferMode", this.transferMode.ordinal());
    }

    @Override
    public Set<IFortronFrequency> getLinkedDevices() {
        final Set<IFortronFrequency> fortronBlocks = new HashSet<>();
        final Set<IBlockFrequency> frequencyBlocks = FrequencyGrid.instance().get(
            this.getWorldObj(),
            new Vector3(this),
            this.getTransmissionRange(),
            this.getFrequency()
        );
        for (final IBlockFrequency frequencyBlock : frequencyBlocks) {
            if (frequencyBlock instanceof IFortronFrequency) {
                fortronBlocks.add((IFortronFrequency) frequencyBlock);
            }
        }
        return fortronBlocks;
    }

    public boolean isItemValidForSlot(final int slotID, final ItemStack itemStack) {
        if (slotID == 0 || slotID == 1) {
            return itemStack.getItem() instanceof ICard;
        }
        return itemStack.getItem() instanceof IModule;
    }

    public Set<ItemStack> getCards() {
        final Set<ItemStack> cards = new HashSet<>();
        cards.add(super.getCard());
        cards.add(this.getStackInSlot(1));
        return cards;
    }

    public TransferMode getTransferMode() {
        return this.transferMode;
    }

    @Override
    public int getTransmissionRange() {
        return 15
            + this.getModuleCount(ModularForceFieldSystem.itemModuleScale, new int[0]);
    }

    @Override
    public int getTransmissionRate() {
        return 250
            + 50
            * this.getModuleCount(ModularForceFieldSystem.itemModuleSpeed, new int[0]);
    }
}
