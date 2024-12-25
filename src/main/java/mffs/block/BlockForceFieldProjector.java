package mffs.block;

import mffs.base.BlockMachine;
import mffs.tileentity.TileEntityForceFieldProjector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockForceFieldProjector extends BlockMachine {
    public BlockForceFieldProjector() {
        super("projector");
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.8f, 1.0f);
    }

    @Override
    public TileEntity createNewTileEntity(final World world, int meta) {
        return new TileEntityForceFieldProjector();
    }

    @Override
    public boolean onMachineActivated(
        final World world,
        final int i,
        final int j,
        final int k,
        final EntityPlayer entityplayer,
        final int par6,
        final float par7,
        final float par8,
        final float par9
    ) {
        final TileEntityForceFieldProjector tileentity
            = (TileEntityForceFieldProjector) world.getTileEntity(i, j, k);
        return !tileentity.isDisabled()
            && super.onMachineActivated(
                world, i, j, k, entityplayer, par6, par7, par8, par9
            );
    }

    public int getLightValue(
        final IBlockAccess iBlockAccess, final int x, final int y, final int z
    ) {
        final TileEntity tileEntity = iBlockAccess.getTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityForceFieldProjector
            && ((TileEntityForceFieldProjector) tileEntity).getMode() != null) {
            return 10;
        }
        return super.getLightValue(iBlockAccess, x, y, z);
    }
}
