package calclavia.lib;

import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.core.electricity.ElectricityNetworkHelper;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.prefab.implement.IRotatable;
import universalelectricity.prefab.tile.TileEntityElectrical;

public class TileEntityUniversalProducer extends TileEntityElectrical {
    public ElectricityPack produce(double watts) {
        ElectricityPack pack
            = new ElectricityPack(watts / this.getVoltage(), this.getVoltage());
        ElectricityPack remaining
            = ElectricityNetworkHelper.produceFromMultipleSides(this, pack);

        return remaining;
    }

    @Override
    public boolean canConnect(ForgeDirection direction) {
        return this instanceof IRotatable ? direction.ordinal() == this.getBlockMetadata()
                                          : true;
    }
}
