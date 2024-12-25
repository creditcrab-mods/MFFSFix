package mffs.block;

import mffs.tileentity.TileEntityInterdictionMatrix;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockInterdictionMatrix extends BlockMachineBlock {
    public BlockInterdictionMatrix() {
        super("interdictionMatrix");
    }

    @Override
    public TileEntity createNewTileEntity(final World world, int meta) {
        return new TileEntityInterdictionMatrix();
    }
}
