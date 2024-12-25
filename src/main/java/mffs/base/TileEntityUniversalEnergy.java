package mffs.base;

import java.util.EnumSet;

import calclavia.lib.IUniversalEnergyTile;
import cofh.api.energy.IEnergyReceiver;
import cpw.mods.fml.common.Optional;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import mffs.ConversionHelper;
import mffs.tileentity.TileEntityCoercionDeriver;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.electricity.ElectricityNetworkHelper;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.vector.Vector3;

@Optional.Interface(modid = "IC2", iface = "ic2.api.energy.tile.IEnergySink")
public abstract class TileEntityUniversalEnergy extends TileEntityModuleAcceptor
    implements IUniversalEnergyTile, IEnergyReceiver, IEnergySink {
    public double prevWatts;
    public double wattsReceived;

    public TileEntityUniversalEnergy() {
        this.wattsReceived = 0.0;
    }

    @Optional.Method(modid = "IC2")
    @Override
    public void initiate() {
        super.initiate();
        MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
    }

    @Optional.Method(modid = "IC2")
    @Override
    public void invalidate() {
        MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        super.invalidate();
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        this.prevWatts = this.wattsReceived;
        if (!this.worldObj.isRemote) {
            if (!this.isDisabled()) {
                final ElectricityPack electricityPack
                    = ElectricityNetworkHelper.consumeFromMultipleSides(
                        this, this.getConsumingSides(), this.getRequest()
                    );
                this.onReceive(electricityPack);
            } else {
                ElectricityNetworkHelper.consumeFromMultipleSides(
                    this, new ElectricityPack()
                );
            }
        }
    }

    protected EnumSet<ForgeDirection> getConsumingSides() {
        return ElectricityNetworkHelper.getDirections(this);
    }

    public ElectricityPack getRequest() {
        return new ElectricityPack();
    }

    public void onReceive(final ElectricityPack electricityPack) {
        if (UniversalElectricity.isVoltageSensitive
            && electricityPack.voltage > this.getVoltage()) {
            return;
        }
        this.wattsReceived = Math.min(
            this.wattsReceived + electricityPack.getWatts(), this.getWattBuffer()
        );
    }

    public double getWattBuffer() {
        return this.getRequest().getWatts() * 2.0;
    }

    @Override
    public double getVoltage() {
        return 120.0;
    }

    public ElectricityPack produce(double watts) {
        ElectricityPack pack
            = new ElectricityPack(watts / this.getVoltage(), this.getVoltage());
        ElectricityPack remaining
            = ElectricityNetworkHelper.produceFromMultipleSides(this, pack);

        return remaining;
    }

    public int produceRF(int amount) {
        int available = amount;
        for (ForgeDirection dir : ForgeDirection.values()) {
            TileEntity te = new Vector3(this).modifyPositionFromSide(dir).getTileEntity(
                this.worldObj
            );
            if (te instanceof IEnergyReceiver) {
                IEnergyReceiver receiver = (IEnergyReceiver) te;
                if (!receiver.canConnectEnergy(dir.getOpposite()))
                    continue;
                available
                    -= receiver.receiveEnergy(dir.getOpposite(), (available), false);
            }
        }
        return amount - available;
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection side) {
        return canConnect(side);
    }

    @Override
    public int receiveEnergy(ForgeDirection side, int energy, boolean simulate) {
        if (!canConnect(side))
            return 0;
        double toReceive = ConversionHelper.fromRF(energy);
        double free = getWattBuffer() - wattsReceived;
        double toInject = Math.min(toReceive, free);
        if (!simulate) {
            wattsReceived += toInject;
        }
        return (int) Math.ceil(toInject / UniversalElectricity.UE_RF_RATIO);
    }

    @Override
    public int getEnergyStored(ForgeDirection var1) {
        return ConversionHelper.toRF(
            getFortronEnergy() * TileEntityCoercionDeriver.FORTRON_UE_RATIO
        );
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection var1) {
        return ConversionHelper.toRF(
            getFortronCapacity() * TileEntityCoercionDeriver.FORTRON_UE_RATIO
        );
    }

    @Optional.Method(modid = "IC2")
    @Override
    public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction) {
        return getConsumingSides().contains(direction);
    }

    @Optional.Method(modid = "IC2")
    @Override
    public double getDemandedEnergy() {
        return ConversionHelper.toEU(this.getRequest().getWatts());
    }

    @Optional.Method(modid = "IC2")
    @Override
    public int getSinkTier() {
        return 2;
    }

    @Optional.Method(modid = "IC2")
    @Override
    public double injectEnergy(ForgeDirection direction, double i, double voltage) {
        double givenElectricity = ConversionHelper.fromEU(i);
        double rejects = 0.0;
        if (givenElectricity > this.getWattBuffer()) {
            rejects = givenElectricity - this.getRequest().getWatts();
        }
        this.onReceive(
            new ElectricityPack(givenElectricity / this.getVoltage(), this.getVoltage())
        );
        return ConversionHelper.toEU(rejects);
    }
}
