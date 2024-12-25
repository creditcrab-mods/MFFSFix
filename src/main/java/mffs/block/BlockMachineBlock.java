package mffs.block;

import mffs.base.BlockMachine;
import mffs.base.TileEntityBase;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public abstract class BlockMachineBlock extends BlockMachine {
    protected IIcon blockIconTop;
    protected IIcon blockIconOn;
    protected IIcon blockIconTopOn;

    public BlockMachineBlock(final String name) {
        super(name);
    }

    @Override
    public IIcon getIcon(
        final IBlockAccess par1IBlockAccess,
        final int x,
        final int y,
        final int z,
        final int side
    ) {
        final TileEntity tileEntity = par1IBlockAccess.getTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityBase
            && ((TileEntityBase) tileEntity).isActive()) {
            if (side == 0 || side == 1) {
                return this.blockIconTopOn;
            }
            return this.blockIconOn;
        } else {
            if (side == 0 || side == 1) {
                return this.blockIconTop;
            }
            return this.blockIcon;
        }
    }

    @Override
    public void registerBlockIcons(final IIconRegister reg) {
        this.blockIcon = reg.registerIcon(this.getUnlocalizedName().replace("tile.", ""));
        this.blockIconTop
            = reg.registerIcon(this.getUnlocalizedName().replace("tile.", "") + "_top");
        this.blockIconOn
            = reg.registerIcon(this.getUnlocalizedName().replace("tile.", "") + "_on");
        this.blockIconTopOn = reg.registerIcon(
            this.getUnlocalizedName().replace("tile.", "") + "_top_on"
        );
    }

    @Override
    public boolean isOpaqueCube() {
        return true;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return true;
    }

    @Override
    public int getRenderType() {
        return 0;
    }
}
