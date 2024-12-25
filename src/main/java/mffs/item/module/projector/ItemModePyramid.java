package mffs.item.module.projector;

import java.util.HashSet;
import java.util.Set;

import calclavia.lib.CalculationHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mffs.api.IFieldInteraction;
import mffs.api.IProjector;
import mffs.item.mode.ItemMode;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.vector.Region3;

public class ItemModePyramid extends ItemMode {
    public ItemModePyramid() {
        super("modePyramid");
    }

    @Override
    public Set<Vector3> getExteriorPoints(final IFieldInteraction projector) {
        final Set<Vector3> fieldBlocks = new HashSet<>();
        final Vector3 posScale = projector.getPositiveScale();
        final Vector3 negScale = projector.getNegativeScale();
        final int xStretch = posScale.intX() + negScale.intX();
        final int yStretch = posScale.intY() + negScale.intY();
        final int zStretch = posScale.intZ() + negScale.intZ();
        final Vector3 translation = new Vector3(0.0, -negScale.intY(), 0.0);
        for (float y = 0.0f; y <= yStretch; ++y) {
            for (float x = (float) (-xStretch); x <= xStretch; ++x) {
                for (float z = (float) (-zStretch); z <= zStretch; ++z) {
                    final double yTest = y / yStretch * 8.0f;
                    final double xzPositivePlane
                        = (1.0f - x / xStretch - z / zStretch) * 8.0f;
                    final double xzNegativePlane
                        = (1.0f + x / xStretch - z / zStretch) * 8.0f;
                    if (x >= 0.0f && z >= 0.0f
                        && Math.round(xzPositivePlane) == Math.round(yTest)) {
                        fieldBlocks.add(new Vector3(x, y, z).add(translation));
                        fieldBlocks.add(new Vector3(x, y, -z).add(translation));
                    }
                    if (x <= 0.0f && z >= 0.0f
                        && Math.round(xzNegativePlane) == Math.round(yTest)) {
                        fieldBlocks.add(new Vector3(x, y, -z).add(translation));
                        fieldBlocks.add(new Vector3(x, y, z).add(translation));
                    }
                    if (y == 0.0f
                        && Math.abs(x) + Math.abs(z) < (xStretch + yStretch) / 2) {
                        fieldBlocks.add(new Vector3(x, y, z).add(translation));
                    }
                }
            }
        }
        return fieldBlocks;
    }

    @Override
    public Set<Vector3> getInteriorPoints(final IFieldInteraction projector) {
        final Set<Vector3> fieldBlocks = new HashSet<>();
        final Vector3 posScale = projector.getPositiveScale();
        final Vector3 negScale = projector.getNegativeScale();
        final int xStretch = posScale.intX() + negScale.intX();
        final int yStretch = posScale.intY() + negScale.intY();
        final int zStretch = posScale.intZ() + negScale.intZ();
        final Vector3 translation = new Vector3(0.0, -0.4, 0.0);
        for (float x = (float) (-xStretch); x <= xStretch; ++x) {
            for (float z = (float) (-zStretch); z <= zStretch; ++z) {
                for (float y = 0.0f; y <= yStretch; ++y) {
                    final Vector3 position = new Vector3(x, y, z).add(translation);
                    if (this.isInField(
                            projector,
                            Vector3.add(position, new Vector3((TileEntity) projector))
                        )) {
                        fieldBlocks.add(position);
                    }
                }
            }
        }
        return fieldBlocks;
    }

    @Override
    public boolean isInField(final IFieldInteraction projector, final Vector3 position) {
        final Vector3 posScale = projector.getPositiveScale().clone();
        final Vector3 negScale = projector.getNegativeScale().clone();
        final int xStretch = posScale.intX() + negScale.intX();
        final int yStretch = posScale.intY() + negScale.intY();
        final int zStretch = posScale.intZ() + negScale.intZ();
        final Vector3 projectorPos = new Vector3((TileEntity) projector);
        projectorPos.add(projector.getTranslation());
        projectorPos.add(new Vector3(0.0, -negScale.intY() + 1, 0.0));
        final Vector3 relativePosition = position.clone().subtract(projectorPos);
        CalculationHelper.rotateByAngle(
            relativePosition, -projector.getRotationYaw(), -projector.getRotationPitch()
        );
        final Region3 region = new Region3(negScale.multiply(-1.0), posScale);
        return region.isIn(relativePosition) && relativePosition.y > 0.0
            && 1.0 - Math.abs(relativePosition.x) / xStretch
                - Math.abs(relativePosition.z) / zStretch
            > relativePosition.y / yStretch;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void render(
        final IProjector projector,
        final double x,
        final double y,
        final double z,
        final float f,
        final long ticks
    ) {
        final Tessellator tessellator = Tessellator.instance;
        GL11.glPushMatrix();
        GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
        final float height = 0.5f;
        final float width = 0.3f;
        final int uvMaxX = 2;
        final int uvMaxY = 2;
        final Vector3 translation = new Vector3(0.0, -0.4, 0.0);
        tessellator.startDrawing(6);
        tessellator.setColorRGBA(72, 198, 255, 255);
        tessellator.addVertexWithUV(
            0.0 + translation.x, 0.0 + translation.y, 0.0 + translation.z, 0.0, 0.0
        );
        tessellator.addVertexWithUV(
            -width + translation.x,
            height + translation.y,
            -width + translation.z,
            (double) (-uvMaxX),
            (double) (-uvMaxY)
        );
        tessellator.addVertexWithUV(
            -width + translation.x,
            height + translation.y,
            width + translation.z,
            (double) (-uvMaxX),
            (double) uvMaxY
        );
        tessellator.addVertexWithUV(
            width + translation.x,
            height + translation.y,
            width + translation.z,
            (double) uvMaxX,
            (double) uvMaxY
        );
        tessellator.addVertexWithUV(
            width + translation.x,
            height + translation.y,
            -width + translation.z,
            (double) uvMaxX,
            (double) (-uvMaxY)
        );
        tessellator.addVertexWithUV(
            -width + translation.x,
            height + translation.y,
            -width + translation.z,
            (double) (-uvMaxX),
            (double) (-uvMaxY)
        );
        tessellator.draw();
        GL11.glPopMatrix();
    }
}
