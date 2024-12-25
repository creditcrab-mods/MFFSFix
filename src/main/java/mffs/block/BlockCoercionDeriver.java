package mffs.block;

import mffs.base.BlockMachine;
import mffs.tileentity.TileEntityCoercionDeriver;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCoercionDeriver extends BlockMachine {
    public BlockCoercionDeriver() {
        super("coercionDeriver");
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.8f, 1.0f);
    }

    @Override
    public TileEntity createNewTileEntity(final World world, int meta) {
        return new TileEntityCoercionDeriver();
    }
}
