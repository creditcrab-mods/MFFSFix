package mffs.render.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelForceFieldProjector extends ModelBase {
    ModelRenderer top;
    ModelRenderer axle;
    ModelRenderer bottom;
    ModelRenderer thingfront;
    ModelRenderer thingback;
    ModelRenderer thingright;
    ModelRenderer thingleft;
    ModelRenderer attacherbig1;
    ModelRenderer attacherbig2;
    ModelRenderer attachersmall3;
    ModelRenderer attachersmall4;
    ModelRenderer attachersmall2;
    ModelRenderer attachersmall_1;
    ModelRenderer corner1;
    ModelRenderer corner2;
    ModelRenderer corner3;
    ModelRenderer corner4;
    ModelRenderer lense;
    ModelRenderer lensesidefront;
    ModelRenderer lensesideback;
    ModelRenderer lensesideright;
    ModelRenderer lensesideleft;
    ModelRenderer lensecorner1;
    ModelRenderer lensecorner2;
    ModelRenderer lensecorner3;
    ModelRenderer lensecorner4;

    public ModelForceFieldProjector() {
        super.textureWidth = 128;
        super.textureHeight = 64;
        (this.top = new ModelRenderer((ModelBase) this, 0, 0))
            .addBox(-8.0f, -4.0f, -8.0f, 16, 2, 16);
        this.top.setRotationPoint(0.0f, 16.0f, 0.0f);
        this.top.setTextureSize(128, 64);
        this.top.mirror = true;
        this.setRotation(this.top, 0.0f, 0.0f, 0.0f);
        (this.axle = new ModelRenderer((ModelBase) this, 16, 26))
            .addBox(-1.0f, -2.0f, -1.0f, 2, 8, 2);
        this.axle.setRotationPoint(0.0f, 16.0f, 0.0f);
        this.axle.setTextureSize(128, 64);
        this.axle.mirror = true;
        this.setRotation(this.axle, 0.0f, 0.0f, 0.0f);
        (this.bottom = new ModelRenderer((ModelBase) this, 0, 44))
            .addBox(-8.0f, 6.0f, -8.0f, 16, 2, 16);
        this.bottom.setRotationPoint(0.0f, 16.0f, 0.0f);
        this.bottom.setTextureSize(128, 64);
        this.bottom.mirror = true;
        this.setRotation(this.bottom, 0.0f, 0.0f, 0.0f);
        (this.thingfront = new ModelRenderer((ModelBase) this, 0, 20))
            .addBox(-2.0f, -2.0f, -7.0f, 4, 8, 4);
        this.thingfront.setRotationPoint(0.0f, 16.0f, 0.0f);
        this.thingfront.setTextureSize(128, 64);
        this.thingfront.mirror = true;
        this.setRotation(this.thingfront, 0.0f, 0.0f, 0.0f);
        (this.thingback = new ModelRenderer((ModelBase) this, 0, 20))
            .addBox(-2.0f, -2.0f, 3.0f, 4, 8, 4);
        this.thingback.setRotationPoint(0.0f, 16.0f, 0.0f);
        this.thingback.setTextureSize(128, 64);
        this.thingback.mirror = true;
        this.setRotation(this.thingback, 0.0f, 0.0f, 0.0f);
        (this.thingright = new ModelRenderer((ModelBase) this, 0, 20))
            .addBox(-6.0f, -2.0f, -2.0f, 4, 8, 4);
        this.thingright.setRotationPoint(0.0f, 16.0f, 0.0f);
        this.thingright.setTextureSize(128, 64);
        this.thingright.mirror = true;
        this.setRotation(this.thingright, 0.0f, 0.0f, 0.0f);
        (this.thingleft = new ModelRenderer((ModelBase) this, 0, 20))
            .addBox(2.0f, -2.0f, -2.0f, 4, 8, 4);
        this.thingleft.setRotationPoint(0.0f, 16.0f, 0.0f);
        this.thingleft.setTextureSize(128, 64);
        this.thingleft.mirror = true;
        this.setRotation(this.thingleft, 0.0f, 0.0f, 0.0f);
        (this.attacherbig1 = new ModelRenderer((ModelBase) this, 16, 20))
            .addBox(-7.0f, -1.0f, -3.0f, 14, 1, 6);
        this.attacherbig1.setRotationPoint(0.0f, 16.0f, 0.0f);
        this.attacherbig1.setTextureSize(128, 64);
        this.attacherbig1.mirror = true;
        this.setRotation(this.attacherbig1, 0.0f, 0.0f, 0.0f);
        (this.attacherbig2 = new ModelRenderer((ModelBase) this, 16, 20))
            .addBox(-7.0f, 4.0f, -3.0f, 14, 1, 6);
        this.attacherbig2.setRotationPoint(0.0f, 16.0f, 0.0f);
        this.attacherbig2.setTextureSize(128, 64);
        this.attacherbig2.mirror = true;
        this.setRotation(this.attacherbig2, 0.0f, 0.0f, 0.0f);
        (this.attachersmall3 = new ModelRenderer((ModelBase) this, 16, 36))
            .addBox(-3.0f, -1.0f, -8.0f, 6, 1, 5);
        this.attachersmall3.setRotationPoint(0.0f, 16.0f, 0.0f);
        this.attachersmall3.setTextureSize(128, 64);
        this.attachersmall3.mirror = true;
        this.setRotation(this.attachersmall3, 0.0f, 0.0f, 0.0f);
        (this.attachersmall4 = new ModelRenderer((ModelBase) this, 16, 36))
            .addBox(-3.0f, 4.0f, -8.0f, 6, 1, 5);
        this.attachersmall4.setRotationPoint(0.0f, 16.0f, 0.0f);
        this.attachersmall4.setTextureSize(128, 64);
        this.attachersmall4.mirror = true;
        this.setRotation(this.attachersmall4, 0.0f, 0.0f, 0.0f);
        (this.attachersmall2 = new ModelRenderer((ModelBase) this, 16, 36))
            .addBox(-3.0f, 4.0f, 3.0f, 6, 1, 5);
        this.attachersmall2.setRotationPoint(0.0f, 16.0f, 0.0f);
        this.attachersmall2.setTextureSize(128, 64);
        this.attachersmall2.mirror = true;
        this.setRotation(this.attachersmall2, 0.0f, 0.0f, 0.0f);
        (this.attachersmall_1 = new ModelRenderer((ModelBase) this, 16, 36))
            .addBox(-3.0f, -1.0f, 3.0f, 6, 1, 5);
        this.attachersmall_1.setRotationPoint(0.0f, 16.0f, 0.0f);
        this.attachersmall_1.setTextureSize(128, 64);
        this.attachersmall_1.mirror = true;
        this.setRotation(this.attachersmall_1, 0.0f, 0.0f, 0.0f);
        (this.corner1 = new ModelRenderer((ModelBase) this, 38, 32))
            .addBox(6.0f, -2.0f, -8.0f, 2, 8, 2);
        this.corner1.setRotationPoint(0.0f, 16.0f, 0.0f);
        this.corner1.setTextureSize(128, 64);
        this.corner1.mirror = true;
        this.setRotation(this.corner1, 0.0f, 0.0f, 0.0f);
        (this.corner2 = new ModelRenderer((ModelBase) this, 46, 32))
            .addBox(6.0f, -2.0f, 6.0f, 2, 8, 2);
        this.corner2.setRotationPoint(0.0f, 16.0f, 0.0f);
        this.corner2.setTextureSize(128, 64);
        this.corner2.mirror = true;
        this.setRotation(this.corner2, 0.0f, 0.0f, 0.0f);
        (this.corner3 = new ModelRenderer((ModelBase) this, 0, 32))
            .addBox(-8.0f, -2.0f, 6.0f, 2, 8, 2);
        this.corner3.setRotationPoint(0.0f, 16.0f, 0.0f);
        this.corner3.setTextureSize(128, 64);
        this.corner3.mirror = true;
        this.setRotation(this.corner3, 0.0f, 0.0f, 0.0f);
        (this.corner4 = new ModelRenderer((ModelBase) this, 8, 32))
            .addBox(-8.0f, -2.0f, -8.0f, 2, 8, 2);
        this.corner4.setRotationPoint(0.0f, 16.0f, 0.0f);
        this.corner4.setTextureSize(128, 64);
        this.corner4.mirror = true;
        this.setRotation(this.corner4, 0.0f, 0.0f, 0.0f);
        (this.lense = new ModelRenderer((ModelBase) this, 96, 0))
            .addBox(-4.0f, -5.0f, -4.0f, 8, 1, 8);
        this.lense.setRotationPoint(0.0f, 16.0f, 0.0f);
        this.lense.setTextureSize(128, 64);
        this.lense.mirror = true;
        this.setRotation(this.lense, 0.0f, 0.0f, 0.0f);
        (this.lensesidefront = new ModelRenderer((ModelBase) this, 64, 5))
            .addBox(-3.0f, -6.0f, -5.0f, 6, 2, 1);
        this.lensesidefront.setRotationPoint(0.0f, 16.0f, 0.0f);
        this.lensesidefront.setTextureSize(128, 64);
        this.lensesidefront.mirror = true;
        this.setRotation(this.lensesidefront, 0.0f, 0.0f, 0.0f);
        (this.lensesideback = new ModelRenderer((ModelBase) this, 64, 5))
            .addBox(-3.0f, -6.0f, 4.0f, 6, 2, 1);
        this.lensesideback.setRotationPoint(0.0f, 16.0f, 0.0f);
        this.lensesideback.setTextureSize(128, 64);
        this.lensesideback.mirror = true;
        this.setRotation(this.lensesideback, 0.0f, 0.0f, 0.0f);
        (this.lensesideright = new ModelRenderer((ModelBase) this, 64, 8))
            .addBox(-5.0f, -6.0f, -3.0f, 1, 2, 6);
        this.lensesideright.setRotationPoint(0.0f, 16.0f, 0.0f);
        this.lensesideright.setTextureSize(128, 64);
        this.lensesideright.mirror = true;
        this.setRotation(this.lensesideright, 0.0f, 0.0f, 0.0f);
        (this.lensesideleft = new ModelRenderer((ModelBase) this, 64, 8))
            .addBox(4.0f, -6.0f, -3.0f, 1, 2, 6);
        this.lensesideleft.setRotationPoint(0.0f, 16.0f, 0.0f);
        this.lensesideleft.setTextureSize(128, 64);
        this.lensesideleft.mirror = true;
        this.setRotation(this.lensesideleft, 0.0f, 0.0f, 0.0f);
        (this.lensecorner1 = new ModelRenderer((ModelBase) this, 64, 16))
            .addBox(3.0f, -6.0f, -4.0f, 1, 2, 1);
        this.lensecorner1.setRotationPoint(0.0f, 16.0f, 0.0f);
        this.lensecorner1.setTextureSize(128, 64);
        this.lensecorner1.mirror = true;
        this.setRotation(this.lensecorner1, 0.0f, 0.0f, 0.0f);
        (this.lensecorner2 = new ModelRenderer((ModelBase) this, 64, 16))
            .addBox(3.0f, -6.0f, 3.0f, 1, 2, 1);
        this.lensecorner2.setRotationPoint(0.0f, 16.0f, 0.0f);
        this.lensecorner2.setTextureSize(128, 64);
        this.lensecorner2.mirror = true;
        this.setRotation(this.lensecorner2, 0.0f, 0.0f, 0.0f);
        (this.lensecorner3 = new ModelRenderer((ModelBase) this, 64, 16))
            .addBox(-4.0f, -6.0f, 3.0f, 1, 2, 1);
        this.lensecorner3.setRotationPoint(0.0f, 16.0f, 0.0f);
        this.lensecorner3.setTextureSize(128, 64);
        this.lensecorner3.mirror = true;
        this.setRotation(this.lensecorner3, 0.0f, 0.0f, 0.0f);
        (this.lensecorner4 = new ModelRenderer((ModelBase) this, 64, 16))
            .addBox(-4.0f, -6.0f, -4.0f, 1, 2, 1);
        this.lensecorner4.setRotationPoint(0.0f, 16.0f, 0.0f);
        this.lensecorner4.setTextureSize(128, 64);
        this.lensecorner4.mirror = true;
        this.setRotation(this.lensecorner4, 0.0f, 0.0f, 0.0f);
    }

    public void render(final float rotation, final float f5) {
        this.top.render(f5);
        this.axle.render(f5);
        this.bottom.render(f5);
        GL11.glPushMatrix();
        GL11.glRotatef(rotation, 0.0f, 1.0f, 0.0f);
        this.thingfront.render(f5);
        this.attachersmall3.render(f5);
        this.thingback.render(f5);
        this.thingright.render(f5);
        this.thingleft.render(f5);
        this.attacherbig1.render(f5);
        this.attacherbig2.render(f5);
        this.attachersmall4.render(f5);
        this.attachersmall2.render(f5);
        this.attachersmall_1.render(f5);
        GL11.glPopMatrix();
        this.corner1.render(f5);
        this.corner2.render(f5);
        this.corner3.render(f5);
        this.corner4.render(f5);
        this.lense.render(f5);
        this.lensesidefront.render(f5);
        this.lensesideback.render(f5);
        this.lensesideright.render(f5);
        this.lensesideleft.render(f5);
        this.lensecorner1.render(f5);
        this.lensecorner2.render(f5);
        this.lensecorner3.render(f5);
        this.lensecorner4.render(f5);
    }

    private void
    setRotation(final ModelRenderer model, final float x, final float y, final float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}
