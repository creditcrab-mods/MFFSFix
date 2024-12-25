package mffs.item.module.projector;

import java.util.HashSet;
import java.util.Set;

import calclavia.lib.CalculationHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mffs.api.IFieldInteraction;
import mffs.api.IProjector;
import mffs.item.mode.ItemMode;
import mffs.render.model.ModelCube;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import universalelectricity.core.vector.Vector3;

public class ItemModeCylinder extends ItemMode {
    public ItemModeCylinder() {
        super("modeCylinder");
    }

    @Override
    public Set<Vector3> getExteriorPoints(final IFieldInteraction projector) {
        final Set<Vector3> fieldBlocks = new HashSet<>();
        final Vector3 posScale = projector.getPositiveScale();
        final Vector3 negScale = projector.getNegativeScale();
        final int radius
            = (posScale.intX() + negScale.intX() + posScale.intZ() + negScale.intZ()) / 2;
        final int height = posScale.intY() + negScale.intY();
        for (float x = (float) (-radius); x <= radius; ++x) {
            for (float z = (float) (-radius); z <= radius; ++z) {
                for (float y = 0.0f; y < height; ++y) {
                    if ((y == 0.0f || y == height - 1)
                        && x * x + z * z + 0.0f <= radius * radius) {
                        fieldBlocks.add(new Vector3(x, y, z));
                    }
                    if (x * x + z * z + 0.0f <= radius * radius
                        && x * x + z * z + 0.0f >= (radius - 1) * (radius - 1)) {
                        fieldBlocks.add(new Vector3(x, y, z));
                    }
                }
            }
        }
        return fieldBlocks;
    }

    @Override
    public Set<Vector3> getInteriorPoints(final IFieldInteraction projector) {
        final Set<Vector3> fieldBlocks = new HashSet<>();
        final Vector3 translation = projector.getTranslation();
        final Vector3 posScale = projector.getPositiveScale();
        final Vector3 negScale = projector.getNegativeScale();
        final int radius
            = (posScale.intX() + negScale.intX() + posScale.intZ() + negScale.intZ()) / 2;
        final int height = posScale.intY() + negScale.intY();
        for (int x = -radius; x <= radius; ++x) {
            for (int z = -radius; z <= radius; ++z) {
                for (int y = 0; y < height; ++y) {
                    final Vector3 position = new Vector3(x, y, z);
                    if (this.isInField(
                            projector,
                            Vector3.add(position, new Vector3((TileEntity) projector))
                                .add(translation)
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
        final Vector3 posScale = projector.getPositiveScale();
        final Vector3 negScale = projector.getNegativeScale();
        final int radius
            = (posScale.intX() + negScale.intX() + posScale.intZ() + negScale.intZ()) / 2;
        final Vector3 projectorPos = new Vector3((TileEntity) projector);
        projectorPos.add(projector.getTranslation());
        final Vector3 relativePosition = position.clone().subtract(projectorPos);
        CalculationHelper.rotateByAngle(
            relativePosition, -projector.getRotationYaw(), -projector.getRotationPitch()
        );
        return relativePosition.x * relativePosition.x
            + relativePosition.z * relativePosition.z + 0.0
            <= radius * radius;
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
        final float scale = 0.15f;
        final float detail = 0.5f;
        GL11.glScalef(scale, scale, scale);
        final float radius = 1.5f;
        int i = 0;
        for (float renderX = -radius; renderX <= radius; renderX += detail) {
            for (float renderZ = -radius; renderZ <= radius; renderZ += detail) {
                for (float renderY = -radius; renderY <= radius; renderY += detail) {
                    if ((renderX * renderX + renderZ * renderZ + 0.0f <= radius * radius
                         && renderX * renderX + renderZ * renderZ + 0.0f
                             >= (radius - 1.0f) * (radius - 1.0f))
                        || ((renderY == 0.0f || renderY == radius - 1.0f)
                            && renderX * renderX + renderZ * renderZ + 0.0f
                                <= radius * radius)) {
                        if (i % 2 == 0) {
                            final Vector3 vector = new Vector3(renderX, renderY, renderZ);
                            GL11.glTranslated(vector.x, vector.y, vector.z);
                            ModelCube.INSTNACE.render();
                            GL11.glTranslated(-vector.x, -vector.y, -vector.z);
                        }
                        ++i;
                    }
                }
            }
        }
    }
}
