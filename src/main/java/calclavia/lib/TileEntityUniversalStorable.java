package calclavia.lib;

import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.prefab.implement.IRotatable;
import universalelectricity.prefab.tile.TileEntityElectricityStorage;

public abstract class TileEntityUniversalStorable
    extends TileEntityElectricityStorage implements IUniversalEnergyTile {
    @Override
    public boolean canConnect(ForgeDirection direction) {
        return this instanceof IRotatable ? direction
                == ForgeDirection.getOrientation(this.getBlockMetadata()).getOpposite()
                                          : true;
    }

    public ForgeDirection getDirection(IBlockAccess world, int x, int y, int z) {
        return ForgeDirection.getOrientation(this.getBlockMetadata());
    }

    public void
    setDirection(World world, int x, int y, int z, ForgeDirection facingDirection) {
        this.worldObj.setBlockMetadataWithNotify(
            this.xCoord, this.yCoord, this.zCoord, facingDirection.ordinal(), 2
        );
    }
}
