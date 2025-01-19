package mffs;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mffs.gui.*;
import mffs.render.*;
import mffs.tileentity.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fluids.Fluid;
import universalelectricity.core.vector.Vector3;

import static mffs.fortron.FortronHelper.FLUID_FORTRON;

public class ClientProxy extends CommonProxy {
    @Override
    public void init() {
        super.init();
        RenderingRegistry.registerBlockHandler((ISimpleBlockRenderingHandler
        ) new RenderBlockHandler());
        RenderingRegistry.registerBlockHandler((ISimpleBlockRenderingHandler
        ) new RenderForceField());
        MinecraftForgeClient.registerItemRenderer(
            ModularForceFieldSystem.itemCardID, (IItemRenderer) new RenderIDCard()
        );
        ClientRegistry.bindTileEntitySpecialRenderer(
            TileEntityFortronCapacitor.class,
            (TileEntitySpecialRenderer) new RenderFortronCapacitor()
        );
        ClientRegistry.bindTileEntitySpecialRenderer(
            TileEntityCoercionDeriver.class,
            (TileEntitySpecialRenderer) new RenderCoercionDeriver()
        );
        ClientRegistry.bindTileEntitySpecialRenderer(
            TileEntityForceManipulator.class,
            (TileEntitySpecialRenderer) new RenderForceManipulator()
        );
        ClientRegistry.bindTileEntitySpecialRenderer(
            TileEntityForceFieldProjector.class,
            (TileEntitySpecialRenderer) new RenderForceFieldProjector()
        );
    }

    @Override
    public World getClientWorld() {
        return (World) FMLClientHandler.instance().getClient().theWorld;
    }

    @Override
    public Object getClientGuiElement(
        final int ID,
        final EntityPlayer player,
        final World world,
        final int x,
        final int y,
        final int z
    ) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity != null) {
            if (tileEntity.getClass() == TileEntityFortronCapacitor.class) {
                return new GuiFortronCapacitor(
                    player, (TileEntityFortronCapacitor) tileEntity
                );
            }
            if (tileEntity.getClass() == TileEntityForceFieldProjector.class) {
                return new GuiForceFieldProjector(
                    player, (TileEntityForceFieldProjector) tileEntity
                );
            }
            if (tileEntity.getClass() == TileEntityCoercionDeriver.class) {
                return new GuiCoercionDeriver(
                    player, (TileEntityCoercionDeriver) tileEntity
                );
            }
            if (tileEntity.getClass() == TileEntityBiometricIdentifier.class) {
                return new GuiBiometricIdentifier(
                    player, (TileEntityBiometricIdentifier) tileEntity
                );
            }
            if (tileEntity.getClass() == TileEntityInterdictionMatrix.class) {
                return new GuiInterdictionMatrix(
                    player, (TileEntityInterdictionMatrix) tileEntity
                );
            }
            if (tileEntity.getClass() == TileEntityForceManipulator.class) {
                return new GuiForceManipulator(
                    player, (TileEntityForceManipulator) tileEntity
                );
            }
        }
        return null;
    }

    @Override
    public boolean isOp(final String username) {
        return false;
    }

    @Override
    public void renderBeam(
        final World world,
        final Vector3 position,
        final Vector3 target,
        final float red,
        final float green,
        final float blue,
        final int age
    ) {
        FMLClientHandler.instance().getClient().effectRenderer.addEffect((EntityFX
        ) new FXBeam(world, position, target, red, green, blue, age));
    }

    @Override
    public void renderHologram(
        final World world,
        final Vector3 position,
        final float red,
        final float green,
        final float blue,
        final int age,
        final Vector3 targetPosition
    ) {
        FMLClientHandler.instance().getClient().effectRenderer.addEffect(
            (EntityFX) new FXHologram(world, position, red, green, blue, age)
                .setTarget(targetPosition)
        );
    }

    @Override
    public void renderHologramMoving(
        final World world,
        final Vector3 position,
        final float red,
        final float green,
        final float blue,
        final int age
    ) {
        FMLClientHandler.instance().getClient().effectRenderer.addEffect((EntityFX
        ) new FXHologramMoving(world, position, red, green, blue, age));
    }

    @Override
    public boolean isSneaking() {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        return player.isSneaking();
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void registerIcons(TextureStitchEvent.Pre event){
        if(event.map.getTextureType() == 0){
            registerTexture(event.map,FLUID_FORTRON);
        }

    }

    private void registerTexture(IIconRegister ir, Fluid fluid){
        var name = fluid.getName();
        IIcon fluidIcon = ir.registerIcon("mffs:fortron");
        //IconRegistry.addIcon("Fluid" + name,"mffs:fortron",ir);
        FLUID_FORTRON.setIcons(fluidIcon);
    }
}
