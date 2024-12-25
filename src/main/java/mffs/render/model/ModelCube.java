package mffs.render.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

@SideOnly(Side.CLIENT)
public class ModelCube extends ModelBase {
    public static final ModelCube INSTNACE;
    private ModelRenderer cube;

    public ModelCube() {
        this.cube = new ModelRenderer((ModelBase) this, 0, 0);
        final int size = 16;
        this.cube.addBox(
            (float) (-size / 2),
            (float) (-size / 2),
            (float) (-size / 2),
            size,
            size,
            size
        );
        this.cube.setTextureSize(112, 70);
        this.cube.mirror = true;
    }

    public void render() {
        final float f = 0.0625f;
        this.cube.render(f);
    }

    static {
        INSTNACE = new ModelCube();
    }
}
