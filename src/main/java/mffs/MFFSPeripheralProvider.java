package mffs;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import mffs.base.TileEntityBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class MFFSPeripheralProvider implements IPeripheralProvider {
    @Override
    public IPeripheral getPeripheral(World world, int x, int y, int z, int side) {
        TileEntity te = world.getTileEntity(x, y, z);

        if (!(te instanceof TileEntityBase))
            return null;

        return (IPeripheral)te;
    }
}
