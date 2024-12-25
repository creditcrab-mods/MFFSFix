package mffs.item.mode;

import java.util.HashSet;
import java.util.Set;

import calclavia.lib.CalculationHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mffs.api.IFieldInteraction;
import mffs.api.IProjector;
import mffs.render.model.ModelCube;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.vector.Region3;

public class ItemModeCube extends ItemMode {
    public ItemModeCube(final String name) {
        super(name);
    }

    public ItemModeCube(final int i) {
        this("modeCube");
    }

    @Override
    public Set<Vector3> getExteriorPoints(final IFieldInteraction projector) {
        final Set<Vector3> fieldBlocks = new HashSet<>();
        final Vector3 posScale = projector.getPositiveScale();
        final Vector3 negScale = projector.getNegativeScale();
        for (float x = (float) (-negScale.intX()); x <= posScale.intX(); x += 0.5f) {
            for (float z = (float) (-negScale.intZ()); z <= posScale.intZ(); z += 0.5f) {
                for (float y = (float) (-negScale.intY()); y <= posScale.intY();
                     y += 0.5f) {
                    if (y == -negScale.intY() || y == posScale.intY()
                        || x == -negScale.intX() || x == posScale.intX()
                        || z == -negScale.intZ() || z == posScale.intZ()) {
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
        final Vector3 posScale = projector.getPositiveScale();
        final Vector3 negScale = projector.getNegativeScale();
        for (int x = -negScale.intX(); x <= posScale.intX(); ++x) {
            for (int z = -negScale.intZ(); z <= posScale.intZ(); ++z) {
                for (int y = -negScale.intY(); y <= posScale.intY(); ++y) {
                    fieldBlocks.add(new Vector3(x, y, z));
                }
            }
        }
        return fieldBlocks;
    }

    @Override
    public boolean isInField(final IFieldInteraction projector, final Vector3 position) {
        final Vector3 projectorPos = new Vector3((TileEntity) projector);
        projectorPos.add(projector.getTranslation());
        final Vector3 relativePosition = position.clone().subtract(projectorPos);
        CalculationHelper.rotateByAngle(
            relativePosition, -projector.getRotationYaw(), -projector.getRotationPitch()
        );
        final Region3 region = new Region3(
            projector.getNegativeScale().clone().multiply(-1.0),
            projector.getPositiveScale()
        );
        return region.isIn(relativePosition);
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
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        ModelCube.INSTNACE.render();
    }
}
