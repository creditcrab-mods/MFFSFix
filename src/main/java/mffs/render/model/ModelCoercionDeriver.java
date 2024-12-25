package mffs.render.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelCoercionDeriver extends ModelBase {
    ModelRenderer corner1;
    ModelRenderer corner2;
    ModelRenderer corner3;
    ModelRenderer corner4;
    ModelRenderer Bout;
    ModelRenderer Baout;
    ModelRenderer Fout;
    ModelRenderer Tout;
    ModelRenderer Core;
    ModelRenderer Movingthingright;
    ModelRenderer Movingthingleft;
    ModelRenderer bottom;

    public ModelCoercionDeriver() {
        super.textureWidth = 64;
        super.textureHeight = 32;
        (this.corner1 = new ModelRenderer((ModelBase) this, 52, 0))
            .addBox(3.0f, 16.0f, 3.0f, 3, 6, 3);
        this.corner1.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.corner1.setTextureSize(64, 32);
        this.corner1.mirror = true;
        this.setRotation(this.corner1, 0.0f, 0.0f, 0.0f);
        (this.corner2 = new ModelRenderer((ModelBase) this, 52, 0))
            .addBox(-6.0f, 16.0f, 3.0f, 3, 6, 3);
        this.corner2.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.corner2.setTextureSize(64, 32);
        this.corner2.mirror = true;
        this.setRotation(this.corner2, 0.0f, 0.0f, 0.0f);
        (this.corner3 = new ModelRenderer((ModelBase) this, 52, 0))
            .addBox(-6.0f, 16.0f, -6.0f, 3, 6, 3);
        this.corner3.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.corner3.setTextureSize(64, 32);
        this.corner3.mirror = true;
        this.setRotation(this.corner3, 0.0f, 0.0f, 0.0f);
        (this.corner4 = new ModelRenderer((ModelBase) this, 52, 0))
            .addBox(3.0f, 16.0f, -6.0f, 3, 6, 3);
        this.corner4.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.corner4.setTextureSize(64, 32);
        this.corner4.mirror = true;
        this.setRotation(this.corner4, 0.0f, 0.0f, 0.0f);
        (this.Bout = new ModelRenderer((ModelBase) this, 24, 19))
            .addBox(-2.0f, 21.0f, -2.0f, 4, 1, 4);
        this.Bout.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.Bout.setTextureSize(64, 32);
        this.Bout.mirror = true;
        this.setRotation(this.Bout, 0.0f, 0.0f, 0.0f);
        (this.Baout = new ModelRenderer((ModelBase) this, 24, 14))
            .addBox(-2.0f, 14.0f, 3.0f, 4, 4, 1);
        this.Baout.setRotationPoint(0.0f, 2.0f, 0.0f);
        this.Baout.setTextureSize(64, 32);
        this.Baout.mirror = true;
        this.setRotation(this.Baout, 0.0f, 0.0f, 0.0f);
        (this.Fout = new ModelRenderer((ModelBase) this, 24, 14))
            .addBox(-2.0f, 14.0f, -4.0f, 4, 4, 1);
        this.Fout.setRotationPoint(0.0f, 2.0f, 0.0f);
        this.Fout.setTextureSize(64, 32);
        this.Fout.mirror = true;
        this.setRotation(this.Fout, 0.0f, 0.0f, 0.0f);
        (this.Tout = new ModelRenderer((ModelBase) this, 24, 19))
            .addBox(-2.0f, 14.0f, -2.0f, 4, 1, 4);
        this.Tout.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.Tout.setTextureSize(64, 32);
        this.Tout.mirror = true;
        this.setRotation(this.Tout, 0.0f, 0.0f, 0.0f);
        (this.Core = new ModelRenderer((ModelBase) this, 0, 14))
            .addBox(-3.0f, 15.0f, -3.0f, 6, 6, 6);
        this.Core.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.Core.setTextureSize(64, 32);
        this.Core.mirror = true;
        this.setRotation(this.Core, 0.0f, 0.0f, 0.0f);
        (this.Movingthingright = new ModelRenderer((ModelBase) this, 46, 23))
            .addBox(-3.0f, -1.0f, -3.0f, 3, 3, 6);
        this.Movingthingright.setRotationPoint(-3.0f, 20.0f, 0.0f);
        this.Movingthingright.setTextureSize(64, 32);
        this.Movingthingright.mirror = true;
        this.setRotation(this.Movingthingright, 0.0f, 0.0f, 0.0f);
        (this.Movingthingleft = new ModelRenderer((ModelBase) this, 46, 23))
            .addBox(0.0f, -1.0f, -3.0f, 3, 3, 6);
        this.Movingthingleft.setRotationPoint(3.0f, 20.0f, 0.0f);
        this.Movingthingleft.setTextureSize(64, 32);
        this.Movingthingleft.mirror = true;
        this.setRotation(this.Movingthingleft, 0.0f, 0.0f, 0.0f);
        (this.bottom = new ModelRenderer((ModelBase) this, 0, 0))
            .addBox(-6.0f, 22.0f, -6.0f, 12, 2, 12);
        this.bottom.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.bottom.setTextureSize(64, 32);
        this.bottom.mirror = true;
        this.setRotation(this.bottom, 0.0f, 0.0f, 0.0f);
    }

    public void render(final float movement, final float f5) {
        this.corner1.render(f5);
        this.corner2.render(f5);
        this.corner3.render(f5);
        this.corner4.render(f5);
        this.Bout.render(f5);
        this.Baout.render(f5);
        this.Fout.render(f5);
        GL11.glPushMatrix();
        GL11.glRotatef(movement, 0.0f, 1.0f, 0.0f);
        this.Tout.render(f5);
        GL11.glPopMatrix();
        this.Core.render(f5);
        this.Movingthingright.render(f5);
        this.Movingthingleft.render(f5);
        this.bottom.render(f5);
    }

    private void
    setRotation(final ModelRenderer model, final float x, final float y, final float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}
