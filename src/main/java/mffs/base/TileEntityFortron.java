package mffs.base;

import mffs.MFFSHelper;
import mffs.Settings;
import mffs.TransferMode;
import mffs.api.ISpecialForceManipulation;
import mffs.api.card.ICard;
import mffs.api.fortron.IFortronFrequency;
import mffs.fortron.FortronHelper;
import mffs.fortron.FrequencyGrid;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import universalelectricity.core.vector.Vector3;

public abstract class TileEntityFortron extends TileEntityFrequency
    implements IFluidHandler, IFortronFrequency, ISpecialForceManipulation {
    protected FluidTank fortronTank;
    private boolean markSendFortron;

    public TileEntityFortron() {
        this.fortronTank = new FluidTank(FortronHelper.FLUID_FORTRON, 0, 1000);
        this.markSendFortron = true;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (!Settings.CONSERVE_PACKETS && super.ticks % 60L == 0L) {
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        }
    }

    @Override
    public void invalidate() {
        if (this.markSendFortron) {
            MFFSHelper.transferFortron(
                this,
                FrequencyGrid.instance().getFortronTiles(
                    this.worldObj, new Vector3(this), 100, this.getFrequency()
                ),
                TransferMode.DRAIN,
                Integer.MAX_VALUE
            );
        }
        super.invalidate();
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setInteger("fortron", this.fortronTank.getFluidAmount());
        nbt.setBoolean("isActive", this.isActive());

        return new S35PacketUpdateTileEntity(
            this.xCoord, this.yCoord, this.zCoord, this.getBlockMetadata(), nbt
        );
    }

    @Override
    public void onDataPacket(NetworkManager arg0, S35PacketUpdateTileEntity arg1) {
        NBTTagCompound nbt = arg1.func_148857_g();

        this.fortronTank.setFluid(
            new FluidStack(FortronHelper.FLUID_FORTRON, nbt.getInteger("fortron"))
        );
        this.isActive = nbt.getBoolean("isActive");
    }

    @Override
    public boolean preMove(final int x, final int y, final int z) {
        return true;
    }

    @Override
    public void move(final int x, final int y, final int z) {
        this.markSendFortron = false;
    }

    @Override
    public void postMove() {}

    @Override
    public void readFromNBT(final NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.fortronTank.setFluid(
            FluidStack.loadFluidStackFromNBT(nbt.getCompoundTag("fortron"))
        );
    }

    @Override
    public void writeToNBT(final NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        if (this.fortronTank.getFluid() != null) {
            final NBTTagCompound fortronCompound = new NBTTagCompound();
            this.fortronTank.getFluid().writeToNBT(fortronCompound);
            nbt.setTag("fortron", (NBTBase) fortronCompound);
        }
    }

    @Override
    public int
    fill(final ForgeDirection from, final FluidStack resource, final boolean doFill) {
        if (resource.getFluid() == FortronHelper.FLUID_FORTRON) {
            return this.fortronTank.fill(resource, doFill);
        }
        return 0;
    }

    @Override
    public boolean canFill(ForgeDirection arg0, Fluid arg1) {
        return arg1 == FortronHelper.FLUID_FORTRON
            && this.fortronTank.getFluidAmount() < this.fortronTank.getCapacity();
    }

    @Override
    public FluidStack
    drain(final ForgeDirection from, final int maxDrain, final boolean doDrain) {
        return this.fortronTank.drain(maxDrain, doDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection arg0, FluidStack arg1, boolean arg2) {
        if (arg1.getFluid() != FortronHelper.FLUID_FORTRON)
            return null;

        return this.fortronTank.drain(arg1.amount, arg2);
    }

    @Override
    public boolean canDrain(ForgeDirection arg0, Fluid arg1) {
        return arg1 == FortronHelper.FLUID_FORTRON
            && this.fortronTank.getFluidAmount() > 0;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection arg0) {
        return new FluidTankInfo[] { new FluidTankInfo(this.fortronTank) };
    }

    public void setFortronEnergy(final int joules) {
        this.fortronTank.setFluid(FortronHelper.getFortron(joules));
    }

    public int getFortronEnergy() {
        return FortronHelper.getAmount(this.fortronTank);
    }

    public int getFortronCapacity() {
        return this.fortronTank.getCapacity();
    }

    public int requestFortron(final int amount, final boolean doUse) {
        return FortronHelper.getAmount(this.fortronTank.drain(amount, doUse));
    }

    public int provideFortron(final int joules, final boolean doUse) {
        return this.fortronTank.fill(FortronHelper.getFortron(joules), doUse);
    }

    public ItemStack getCard() {
        final ItemStack itemStack = this.getStackInSlot(0);
        if (itemStack != null && itemStack.getItem() instanceof ICard) {
            return itemStack;
        }
        return null;
    }
}
