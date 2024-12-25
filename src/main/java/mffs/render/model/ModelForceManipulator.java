package mffs.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelForceManipulator extends ModelBase {
    ModelRenderer ElectrodePillar;
    ModelRenderer ElectrodeBase;
    ModelRenderer ElectrodeNode;
    ModelRenderer WallBottom;
    ModelRenderer WallFront;
    ModelRenderer WallBack;
    ModelRenderer WallLeft;
    ModelRenderer WallRight;
    ModelRenderer WallTop;

    public ModelForceManipulator() {
        super.textureWidth = 128;
        super.textureHeight = 128;
        (this.ElectrodePillar = new ModelRenderer((ModelBase) this, 0, 32))
            .addBox(0.0f, 0.0f, 0.0f, 3, 3, 3);
        this.ElectrodePillar.setRotationPoint(-1.5f, 19.0f, -1.5f);
        this.ElectrodePillar.setTextureSize(128, 128);
        this.ElectrodePillar.mirror = true;
        this.setRotation(this.ElectrodePillar, 0.0f, 0.0f, 0.0f);
        (this.ElectrodeBase = new ModelRenderer((ModelBase) this, 0, 39))
            .addBox(0.0f, 0.0f, 0.0f, 7, 2, 7);
        this.ElectrodeBase.setRotationPoint(-3.5f, 21.5f, -3.5f);
        this.ElectrodeBase.setTextureSize(128, 128);
        this.ElectrodeBase.mirror = true;
        this.setRotation(this.ElectrodeBase, 0.0f, 0.0f, 0.0f);
        (this.ElectrodeNode = new ModelRenderer((ModelBase) this, 0, 49))
            .addBox(0.0f, 0.0f, 0.0f, 5, 5, 5);
        this.ElectrodeNode.setRotationPoint(-2.5f, 15.0f, -2.5f);
        this.ElectrodeNode.setTextureSize(128, 128);
        this.ElectrodeNode.mirror = true;
        this.setRotation(this.ElectrodeNode, 0.0f, 0.0f, 0.0f);
        (this.WallBottom = new ModelRenderer((ModelBase) this, 0, 0))
            .addBox(0.0f, 0.0f, 0.0f, 16, 1, 16);
        this.WallBottom.setRotationPoint(-8.0f, 23.0f, -8.0f);
        this.WallBottom.setTextureSize(128, 128);
        this.WallBottom.mirror = true;
        this.setRotation(this.WallBottom, 0.0f, 0.0f, 0.0f);
        (this.WallFront = new ModelRenderer((ModelBase) this, 65, 0))
            .addBox(0.0f, 0.0f, 0.0f, 16, 15, 1);
        this.WallFront.setRotationPoint(-8.0f, 8.0f, -8.0f);
        this.WallFront.setTextureSize(128, 128);
        this.WallFront.mirror = true;
        this.setRotation(this.WallFront, 0.0f, 0.0f, 0.0f);
        (this.WallBack = new ModelRenderer((ModelBase) this, 65, 17))
            .addBox(0.0f, 0.0f, 0.0f, 16, 15, 1);
        this.WallBack.setRotationPoint(-8.0f, 8.0f, 7.0f);
        this.WallBack.setTextureSize(128, 128);
        this.WallBack.mirror = true;
        this.setRotation(this.WallBack, 0.0f, 0.0f, 0.0f);
        (this.WallLeft = new ModelRenderer((ModelBase) this, 30, 50))
            .addBox(0.0f, 0.0f, 0.0f, 1, 15, 14);
        this.WallLeft.setRotationPoint(-8.0f, 8.0f, -7.0f);
        this.WallLeft.setTextureSize(128, 128);
        this.WallLeft.mirror = true;
        this.setRotation(this.WallLeft, 0.0f, 0.0f, 0.0f);
        (this.WallRight = new ModelRenderer((ModelBase) this, 30, 19))
            .addBox(0.0f, 0.0f, 0.0f, 1, 15, 14);
        this.WallRight.setRotationPoint(7.0f, 8.0f, -7.0f);
        this.WallRight.setTextureSize(128, 128);
        this.WallRight.mirror = true;
        this.setRotation(this.WallRight, 0.0f, 0.0f, 0.0f);
        (this.WallTop = new ModelRenderer((ModelBase) this, 61, 36))
            .addBox(0.0f, 0.0f, 0.0f, 14, 1, 14);
        this.WallTop.setRotationPoint(-7.0f, 8.0f, -7.0f);
        this.WallTop.setTextureSize(128, 128);
        this.WallTop.mirror = true;
        this.setRotation(this.WallTop, 0.0f, 0.0f, 0.0f);
    }

    public void render(final float f5) {
        this.ElectrodePillar.render(f5);
        this.ElectrodeBase.render(f5);
        this.ElectrodeNode.render(f5);
        this.WallBottom.render(f5);
        this.WallFront.render(f5);
        this.WallBack.render(f5);
        this.WallLeft.render(f5);
        this.WallRight.render(f5);
        this.WallTop.render(f5);
    }

    private void
    setRotation(final ModelRenderer model, final float x, final float y, final float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}
