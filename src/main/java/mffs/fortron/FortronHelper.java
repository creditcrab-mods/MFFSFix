package mffs.fortron;

import cofh.core.render.IconRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mffs.ModularForceFieldSystem;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class FortronHelper {
    public static Fluid FLUID_FORTRON;

    public static FluidStack getFortron(final int amount) {
        return new FluidStack(FLUID_FORTRON, amount);
    }

    public static int getAmount(final FluidStack fluidStack) {
        if (fluidStack != null) {
            return fluidStack.amount;
        }
        return 0;
    }

    public static int getAmount(final FluidTank fortronTank) {
        if (fortronTank != null) {
            return fortronTank.getFluidAmount();
        }
        return 0;
    }

}
