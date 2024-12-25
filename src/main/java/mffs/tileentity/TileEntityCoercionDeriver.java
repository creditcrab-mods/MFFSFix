package mffs.tileentity;

import cpw.mods.fml.common.Optional;
import ic2.api.energy.tile.IEnergySource;
import mffs.ConversionHelper;
import mffs.ModularForceFieldSystem;
import mffs.Settings;
import mffs.api.modules.IModule;
import mffs.base.PacketTile;
import mffs.base.TileEntityUniversalEnergy;
import mffs.fortron.FortronHelper;
import mffs.item.card.ItemCardFrequency;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.item.ElectricItemHelper;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.core.item.IItemElectricityStorage;

@Optional.Interface(modid = "IC2", iface = "ic2.api.energy.tile.IEnergySource")
public class TileEntityCoercionDeriver
    extends TileEntityUniversalEnergy implements IEnergySource {
    public static final int WATTAGE = 1000;
    public static final int REQUIRED_TIME = 200;
    public static final int MULTIPLE_PRODUCTION = 4;
    public static final float FORTRON_UE_RATIO = 6.0f;
    public static final int SLOT_FREQUENCY = 0;
    public static final int SLOT_BATTERY = 1;
    public static final int SLOT_FUEL = 2;
    public int processTime;
    public boolean isInversed;

    public TileEntityCoercionDeriver() {
        this.processTime = 0;
        this.isInversed = false;
        super.capacityBase = 30;
        super.startModuleIndex = 3;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (!this.getWorldObj().isRemote) {
            if (!this.isDisabled() && this.isActive()) {
                if (this.isInversed && Settings.ENABLE_ELECTRICITY) {
                    final double watts
                        = Math.min(this.getFortronEnergy() * FORTRON_UE_RATIO, 1000.0f);
                    final ElectricityPack remainder = this.produce(watts);
                    double electricItemGiven = 0.0;
                    if (remainder.getWatts() > 0.0) {
                        electricItemGiven = ElectricItemHelper.chargeItem(
                            this.getStackInSlot(1),
                            remainder.getWatts(),
                            this.getVoltage()
                        );
                    }
                    double convertedToRF = 0.0;
                    if ((remainder.getWatts() - electricItemGiven) > 0.0) {
                        convertedToRF = ConversionHelper.fromRF(
                            this.produceRF(ConversionHelper.toRF(
                                remainder.getWatts() - electricItemGiven
                            ))
                        );
                    }
                    this.requestFortron(
                        (int) Math.ceil(
                            (watts
                             - (remainder.getWatts() - (electricItemGiven + convertedToRF)
                             ))
                            / FORTRON_UE_RATIO
                        ),
                        true
                    );
                } else {
                    super.wattsReceived += ElectricItemHelper.dechargeItem(
                        this.getStackInSlot(1), 1000.0, this.getVoltage()
                    );
                    if (super.wattsReceived >= 1000.0
                        || (!Settings.ENABLE_ELECTRICITY
                            && this.isItemValidForSlot(2, this.getStackInSlot(2)))) {
                        final int production = this.getProductionRate();
                        super.fortronTank.fill(
                            FortronHelper.getFortron(
                                production + this.getWorldObj().rand.nextInt(production)
                            ),
                            true
                        );
                        if (this.processTime == 0
                            && this.isItemValidForSlot(2, this.getStackInSlot(2))) {
                            this.decrStackSize(2, 1);
                            this.processTime = 200
                                * Math.max(
                                    this.getModuleCount(
                                        ModularForceFieldSystem.itemModuleSpeed,
                                        new int[0]
                                    ) / 20,
                                    1
                                );
                        }
                        if (this.processTime > 0) {
                            --this.processTime;
                            if (this.processTime < 1) {
                                this.processTime = 0;
                            }
                        } else {
                            this.processTime = 0;
                        }
                        super.wattsReceived -= 1000.0;
                    }
                }
            }
        } else if (this.isActive()) {
            ++super.animation;
        }
    }

    public int getProductionRate() {
        if (!this.isDisabled() && this.isActive() && !this.isInversed) {
            int production = 40;
            if (this.processTime > 0) {
                production *= 4;
            }
            return production;
        }
        return 0;
    }

    @Override
    public int getSizeInventory() {
        return 6;
    }

    @Override
    public ElectricityPack getRequest() {
        if (this.canConsume()) {
            return new ElectricityPack(1000.0 / this.getVoltage(), this.getVoltage());
        }
        return super.getRequest();
    }

    public boolean canConsume() {
        return this.isActive() && !this.isInversed
            && FortronHelper.getAmount(super.fortronTank)
            < super.fortronTank.getCapacity();
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setBoolean("isInversed", this.isInversed);
        nbt.setDouble("wattsReceived", super.wattsReceived);
        nbt.setBoolean("isActive", super.isActive);
        nbt.setInteger("fortron", this.fortronTank.getFluidAmount());

        return new S35PacketUpdateTileEntity(
            this.xCoord, this.yCoord, this.zCoord, this.getBlockMetadata(), nbt
        );
    }

    @Override
    public void onDataPacket(NetworkManager arg0, S35PacketUpdateTileEntity arg1) {
        NBTTagCompound nbt = arg1.func_148857_g();

        this.isInversed = nbt.getBoolean("isInversed");
        super.wattsReceived = nbt.getDouble("wattsReceived");

        this.isActive = nbt.getBoolean("isActive");
        this.fortronTank.setFluid(
            new FluidStack(FortronHelper.FLUID_FORTRON, nbt.getInteger("fortron"))
        );
    }

    @Override
    public void onReceivePacket(PacketTile.Type type, final NBTTagCompound nbt) {
        super.onReceivePacket(type, nbt);
        if (type == PacketTile.Type.TOGGLE_MODE) {
            this.isInversed = !this.isInversed;
        }
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.processTime = nbt.getInteger("processTime");
        this.isInversed = nbt.getBoolean("isInversed");
    }

    @Override
    public void writeToNBT(final NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("processTime", this.processTime);
        nbt.setBoolean("isInversed", this.isInversed);
    }

    @Override
    public boolean isItemValidForSlot(final int slotID, final ItemStack itemStack) {
        if (itemStack != null) {
            if (slotID >= super.startModuleIndex) {
                return itemStack.getItem() instanceof IModule;
            }
            switch (slotID) {
                case 0: {
                    return itemStack.getItem() instanceof ItemCardFrequency;
                }
                case 1: {
                    return itemStack.getItem() instanceof IItemElectric;
                }
                case 2: {
                    return itemStack.isItemEqual(new ItemStack(Items.dye, 1, 4))
                        || itemStack.isItemEqual(new ItemStack(Items.quartz));
                }
            }
        }
        return false;
    }

    @Override
    public boolean canConnect(final ForgeDirection direction) {
        return true;
    }

    @Optional.Method(modid = "IC2")
    @Override
    public boolean emitsEnergyTo(TileEntity receiver, ForgeDirection direction) {
        return canConnect(direction) && this.getStackInSlot(1) != null;
    }

    @Optional.Method(modid = "IC2")
    @Override
    public double getOfferedEnergy() {
        ItemStack battery = this.getStackInSlot(1);
        if (battery == null || !(battery.getItem() instanceof IItemElectricityStorage)
            || !this.isInversed)
            return 0;
        IItemElectricityStorage impl = (IItemElectricityStorage) battery.getItem();
        double joules = impl.getJoules(battery);
        return Math.min(ConversionHelper.toEU(joules), 32);
    }

    @Optional.Method(modid = "IC2")
    @Override
    public void drawEnergy(double amount) {
        ItemStack battery = this.getStackInSlot(1);
        if (battery == null || !(battery.getItem() instanceof IItemElectricityStorage))
            return;
        IItemElectricityStorage impl = (IItemElectricityStorage) battery.getItem();
        impl.setJoules(
            impl.getJoules(battery) - ConversionHelper.fromEU(amount), battery
        );
    }

    @Optional.Method(modid = "IC2")
    @Override
    public int getSourceTier() {
        return 1;
    }
}
