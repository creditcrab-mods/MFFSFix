package mffs.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mffs.base.TileEntityBase;
import mffs.render.model.ModelForceManipulator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderForceManipulator extends TileEntitySpecialRenderer {
    public static final String TEXTURE_ON = "forceManipulator_on.png";
    public static final String TEXTURE_OFF = "forceManipulator_off.png";
    public static final ModelForceManipulator MODEL;

    @Override
    public void renderTileEntityAt(
        final TileEntity t, final double x, final double y, final double z, final float f
    ) {
        final TileEntityBase tileEntity = (TileEntityBase) t;
        if (tileEntity.isActive()) {
            this.bindTexture(
                new ResourceLocation("mffs", "textures/models/forceManipulator_on.png")
            );
        } else {
            this.bindTexture(
                new ResourceLocation("mffs", "textures/models/forceManipulator_off.png")
            );
        }
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y + 1.5, z + 0.5);
        GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
        switch (t.getBlockMetadata()) {
            case 0:
                GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
                GL11.glTranslatef(0.0f, -1.0f, -1.0f);
                break;
            case 1:
                GL11.glRotatef(270.0f, 1.0f, 0.0f, 0.0f);
                GL11.glTranslatef(0.0f, -1.0f, 1.0f);
                break;
            case 3:
                GL11.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
                break;
            case 4:
                GL11.glRotatef(270.0f, 0.0f, 1.0f, 0.0f);
                break;
            case 5:
                GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
                break;

            case 2:
            default:
                break;
        }
        RenderForceManipulator.MODEL.render(0.0625f);
        GL11.glPopMatrix();
    }

    static {
        MODEL = new ModelForceManipulator();
    }
}
