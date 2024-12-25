package mffs;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import mffs.gui.GuiBiometricIdentifier;
import mffs.gui.GuiCoercionDeriver;
import mffs.gui.GuiForceFieldProjector;
import mffs.gui.GuiForceManipulator;
import mffs.gui.GuiFortronCapacitor;
import mffs.gui.GuiInterdictionMatrix;
import mffs.render.FXBeam;
import mffs.render.FXHologram;
import mffs.render.FXHologramMoving;
import mffs.render.RenderBlockHandler;
import mffs.render.RenderCoercionDeriver;
import mffs.render.RenderForceField;
import mffs.render.RenderForceFieldProjector;
import mffs.render.RenderForceManipulator;
import mffs.render.RenderFortronCapacitor;
import mffs.render.RenderIDCard;
import mffs.tileentity.TileEntityBiometricIdentifier;
import mffs.tileentity.TileEntityCoercionDeriver;
import mffs.tileentity.TileEntityForceFieldProjector;
import mffs.tileentity.TileEntityForceManipulator;
import mffs.tileentity.TileEntityFortronCapacitor;
import mffs.tileentity.TileEntityInterdictionMatrix;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import universalelectricity.core.vector.Vector3;

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
}
