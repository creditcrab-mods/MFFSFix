package mffs.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mffs.base.TileEntityBase;
import mffs.render.model.ModelFortronCapacitor;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderFortronCapacitor extends TileEntitySpecialRenderer {
    public static final String TEXTURE_ON = "fortronCapacitor_on.png";
    public static final String TEXTURE_OFF = "fortronCapacitor_off.png";
    public static final ModelFortronCapacitor MODEL;

    @Override
    public void renderTileEntityAt(
        final TileEntity t, final double x, final double y, final double z, final float f
    ) {
        final TileEntityBase tileEntity = (TileEntityBase) t;
        if (tileEntity.isActive()) {
            this.bindTexture(
                new ResourceLocation("mffs", "textures/models/fortronCapacitor_on.png")
            );
        } else {
            this.bindTexture(
                new ResourceLocation("mffs", "textures/models/fortronCapacitor_off.png")
            );
        }
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y + 1.95, z + 0.5);
        GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
        GL11.glScalef(1.3f, 1.3f, 1.3f);
        RenderFortronCapacitor.MODEL.render(0.0625f);
        GL11.glPopMatrix();
    }

    static {
        MODEL = new ModelFortronCapacitor();
    }
}
