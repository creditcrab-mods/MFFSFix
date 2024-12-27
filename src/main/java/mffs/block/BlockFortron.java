package mffs.block;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mffs.fortron.FortronHelper;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockFortron extends BlockFluidClassic {

    public BlockFortron() {
        super(FortronHelper.FLUID_FORTRON, Material.water);
        this.setBlockName("mffs:fluidFortron");
        this.setHardness(100.0F);
        this.setLightOpacity(3);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon("mffs:fortron");
        FortronHelper.FLUID_FORTRON.setIcons(this.blockIcon);


    }
}
