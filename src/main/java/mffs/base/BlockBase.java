package mffs.base;

import mffs.MFFSCreativeTab;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.prefab.block.BlockAdvanced;

public abstract class BlockBase extends BlockAdvanced {
    public BlockBase(final String name, final Material material) {
        super(material);
        this.setBlockName("mffs:" + name);
        this.setCreativeTab(MFFSCreativeTab.INSTANCE);
    }

    // anti-bullshit
    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return this.createNewTileEntity(world, meta);
    }
}
