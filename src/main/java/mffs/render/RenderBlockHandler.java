package mffs.render;

import calclavia.lib.render.CalclaviaRenderHelper;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mffs.block.BlockCoercionDeriver;
import mffs.block.BlockForceFieldProjector;
import mffs.block.BlockForceManipulator;
import mffs.block.BlockFortronCapacitor;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderBlockHandler implements ISimpleBlockRenderingHandler {
    public static final int ID;

    @Override
    public void renderInventoryBlock(
        final Block block,
        final int metadata,
        final int modelID,
        final RenderBlocks renderer
    ) {
        if (modelID == RenderBlockHandler.ID) {
            GL11.glPushMatrix();
            if (block instanceof BlockFortronCapacitor) {
                FMLClientHandler.instance().getClient().renderEngine.bindTexture(
                    new ResourceLocation(
                        "mffs", "textures/models/fortronCapacitor_on.png"
                    )
                );
                GL11.glTranslated(0.5, 1.9, 0.5);
                GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
                GL11.glScalef(1.3f, 1.3f, 1.3f);
                RenderFortronCapacitor.MODEL.render(0.0625f);
            } else if (block instanceof BlockForceFieldProjector) {
                FMLClientHandler.instance().getClient().renderEngine.bindTexture(
                    new ResourceLocation("mffs", "textures/models/projector_on.png")
                );
                GL11.glTranslated(0.5, 1.5, 0.5);
                GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
                RenderForceFieldProjector.MODEL.render(0.0f, 0.0625f);
            } else if (block instanceof BlockCoercionDeriver) {
                FMLClientHandler.instance().getClient().renderEngine.bindTexture(
                    new ResourceLocation("mffs", "textures/models/coercionDeriver_on.png")
                );
                GL11.glTranslated(0.5, 1.9, 0.5);
                GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
                GL11.glScalef(1.3f, 1.3f, 1.3f);
                RenderCoercionDeriver.MODEL.render(0.0f, 0.0625f);
            } else if (block instanceof BlockForceManipulator) {
                FMLClientHandler.instance().getClient().renderEngine.bindTexture(
                    new ResourceLocation(
                        "mffs", "textures/models/forceManipulator_on.png"
                    )
                );
                GL11.glTranslated(0.5, 1.4, 0.5);
                GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
                RenderForceManipulator.MODEL.render(0.0625f);
            }
            GL11.glPopMatrix();
        } else {
            CalclaviaRenderHelper.renderNormalBlockAsItem(block, metadata, renderer);
        }
    }

    @Override
    public boolean renderWorldBlock(
        final IBlockAccess iBlockAccess,
        final int x,
        final int y,
        final int z,
        final Block block,
        final int modelID,
        final RenderBlocks renderer
    ) {
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int arg0) {
        return true;
    }

    @Override
    public int getRenderId() {
        return RenderBlockHandler.ID;
    }

    static {
        ID = RenderingRegistry.getNextAvailableRenderId();
    }
}
