package mffs.block;

import mffs.base.BlockMachine;
import mffs.tileentity.TileEntityFortronCapacitor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockFortronCapacitor extends BlockMachine {
    public BlockFortronCapacitor() {
        super("fortronCapacitor");
    }

    @Override
    public TileEntity createNewTileEntity(final World world, int meta) {
        return new TileEntityFortronCapacitor();
    }
}
