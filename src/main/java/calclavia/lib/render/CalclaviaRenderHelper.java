package calclavia.lib.render;

import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import org.lwjgl.opengl.GL11;

public class CalclaviaRenderHelper {
    public static void enableBlending() {
        GL11.glShadeModel(7425);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
    }

    public static void disableBlending() {
        GL11.glShadeModel(7424);
        GL11.glDisable(2848);
        GL11.glDisable(2881);
        GL11.glDisable(3042);
    }

    public static void enableLighting() {
        RenderHelper.enableStandardItemLighting();
    }

    public static void disableLighting() {
        RenderHelper.disableStandardItemLighting();
        OpenGlHelper.setLightmapTextureCoords(
            OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F
        );
    }

    public static void
    renderNormalBlockAsItem(Block block, int metadata, RenderBlocks renderer) {
        Tessellator tessellator = Tessellator.instance;
        block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(block);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(
            block,
            0.0D,
            0.0D,
            0.0D,
            renderer.getBlockIconFromSideAndMetadata(block, 0, metadata)
        );
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(
            block,
            0.0D,
            0.0D,
            0.0D,
            renderer.getBlockIconFromSideAndMetadata(block, 1, metadata)
        );
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(
            block,
            0.0D,
            0.0D,
            0.0D,
            renderer.getBlockIconFromSideAndMetadata(block, 2, metadata)
        );
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(
            block,
            0.0D,
            0.0D,
            0.0D,
            renderer.getBlockIconFromSideAndMetadata(block, 3, metadata)
        );
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(
            block,
            0.0D,
            0.0D,
            0.0D,
            renderer.getBlockIconFromSideAndMetadata(block, 4, metadata)
        );
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(
            block,
            0.0D,
            0.0D,
            0.0D,
            renderer.getBlockIconFromSideAndMetadata(block, 5, metadata)
        );
        tessellator.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

    public static void renderFloatingText(String text, float x, float y, float z) {
        renderFloatingText(text, x, y, z, 16777215);
    }

    public static void
    renderFloatingText(String text, float x, float y, float z, int color) {
        RenderManager renderManager = RenderManager.instance;
        FontRenderer fontRenderer = renderManager.getFontRenderer();
        float scale = 0.027F;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
        GL11.glPushMatrix();
        GL11.glTranslatef(x + 0.0F, y + 2.3F, z);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-scale, -scale, scale);
        GL11.glDisable(2896);
        GL11.glDepthMask(false);
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        Tessellator tessellator = Tessellator.instance;
        byte yOffset = 0;
        GL11.glDisable(3553);
        tessellator.startDrawingQuads();
        int stringMiddle = fontRenderer.getStringWidth(text) / 2;
        tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.5F);
        tessellator.addVertex(
            (double) (-stringMiddle - 1), (double) (-1 + yOffset), 0.0D
        );
        tessellator.addVertex((double) (-stringMiddle - 1), (double) (8 + yOffset), 0.0D);
        tessellator.addVertex((double) (stringMiddle + 1), (double) (8 + yOffset), 0.0D);
        tessellator.addVertex((double) (stringMiddle + 1), (double) (-1 + yOffset), 0.0D);
        tessellator.draw();
        GL11.glEnable(3553);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
        fontRenderer.drawString(
            text, -fontRenderer.getStringWidth(text) / 2, yOffset, color
        );
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        fontRenderer.drawString(
            text, -fontRenderer.getStringWidth(text) / 2, yOffset, color
        );
        GL11.glEnable(2896);
        GL11.glDisable(3042);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }
}
