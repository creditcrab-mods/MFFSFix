package mffs.render;

import calclavia.lib.render.CalclaviaRenderHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mffs.Settings;
import mffs.render.model.ModelForceFieldProjector;
import mffs.tileentity.TileEntityForceFieldProjector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderForceFieldProjector extends TileEntitySpecialRenderer {
    public static final String TEXTURE_ON = "projector_on.png";
    public static final String TEXTURE_OFF = "projector_off.png";
    public static final ModelForceFieldProjector MODEL;

    @Override
    public void renderTileEntityAt(
        final TileEntity t, final double x, final double y, final double z, final float f
    ) {
        if (t instanceof TileEntityForceFieldProjector) {
            final TileEntityForceFieldProjector tileEntity
                = (TileEntityForceFieldProjector) t;
            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5, y + 1.5, z + 0.5);
            if (tileEntity.isActive()) {
                this.bindTexture(
                    new ResourceLocation("mffs", "textures/models/projector_on.png")
                );
            } else {
                this.bindTexture(
                    new ResourceLocation("mffs", "textures/models/projector_off.png")
                );
            }
            GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
            RenderForceFieldProjector.MODEL.render(tileEntity.animation, 0.0625f);
            GL11.glPopMatrix();
            if (tileEntity.getMode() != null) {
                final Tessellator tessellator = Tessellator.instance;
                RenderHelper.disableStandardItemLighting();
                GL11.glPushMatrix();
                GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
                final double xDifference
                    = ((Entity) Minecraft.getMinecraft().thePlayer).posX
                    - (tileEntity.xCoord + 0.5);
                final double zDifference
                    = ((Entity) Minecraft.getMinecraft().thePlayer).posZ
                    - (tileEntity.zCoord + 0.5);
                final float rotatation
                    = (float) Math.toDegrees(Math.atan2(zDifference, xDifference));
                GL11.glRotatef(-rotatation + 27.0f, 0.0f, 1.0f, 0.0f);
                GL11.glDisable(3553);
                GL11.glShadeModel(7425);
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 1);
                GL11.glDisable(3008);
                GL11.glEnable(2884);
                GL11.glDepthMask(false);
                GL11.glPushMatrix();
                tessellator.startDrawing(6);
                final float height = 2.0f;
                final float width = 2.0f;
                tessellator.setColorRGBA(72, 198, 255, 255);
                tessellator.addVertex(0.0, 0.0, 0.0);
                tessellator.setColorRGBA_I(0, 0);
                tessellator.addVertex(
                    -0.866 * width, (double) height, (double) (-0.5f * width)
                );
                tessellator.addVertex(
                    0.866 * width, (double) height, (double) (-0.5f * width)
                );
                tessellator.addVertex(0.0, (double) height, (double) (1.0f * width));
                tessellator.addVertex(
                    -0.866 * width, (double) height, (double) (-0.5f * width)
                );
                tessellator.draw();
                GL11.glPopMatrix();
                GL11.glDepthMask(true);
                GL11.glDisable(2884);
                GL11.glDisable(3042);
                GL11.glShadeModel(7424);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glEnable(3553);
                GL11.glEnable(3008);
                RenderHelper.enableStandardItemLighting();
                GL11.glPopMatrix();
                if (Settings.HIGH_GRAPHICS) {
                    GL11.glPushMatrix();
                    GL11.glTranslated(x + 0.5, y + 1.35, z + 0.5);
                    this.bindTexture(
                        new ResourceLocation("mffs", "textures/models/force_cube.png")
                    );
                    CalclaviaRenderHelper.enableBlending();
                    CalclaviaRenderHelper.disableLighting();
                    GL11.glPushMatrix();
                    GL11.glColor4f(
                        1.0f,
                        1.0f,
                        1.0f,
                        (float) Math.sin(tileEntity.getTicks() / 10.0) / 2.0f + 1.0f
                    );
                    GL11.glTranslatef(
                        0.0f,
                        (float
                        ) Math.sin(Math.toRadians((double) (tileEntity.getTicks() * 3L)))
                            / 7.0f,
                        0.0f
                    );
                    GL11.glRotatef(
                        (float) (tileEntity.getTicks() * 4L), 0.0f, 1.0f, 0.0f
                    );
                    GL11.glRotatef(36.0f + tileEntity.getTicks() * 4L, 0.0f, 1.0f, 1.0f);
                    tileEntity.getMode().render(
                        tileEntity, x, y, z, f, tileEntity.getTicks()
                    );
                    GL11.glPopMatrix();
                    CalclaviaRenderHelper.enableLighting();
                    CalclaviaRenderHelper.disableBlending();
                    GL11.glPopMatrix();
                }
            }
        }
    }

    static {
        MODEL = new ModelForceFieldProjector();
    }
}
