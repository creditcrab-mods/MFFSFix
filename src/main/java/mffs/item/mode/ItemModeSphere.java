package mffs.item.mode;

import java.util.HashSet;
import java.util.Set;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mffs.ModularForceFieldSystem;
import mffs.api.IFieldInteraction;
import mffs.api.IProjector;
import mffs.render.model.ModelCube;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import universalelectricity.core.vector.Vector3;

public class ItemModeSphere extends ItemMode {
    public ItemModeSphere(final int i) {
        super("modeSphere");
    }

    @Override
    public Set<Vector3> getExteriorPoints(final IFieldInteraction projector) {
        final Set<Vector3> fieldBlocks = new HashSet<>();
        final int radius = projector.getModuleCount(
            ModularForceFieldSystem.itemModuleScale, new int[0]
        );
        for (int steps
             = (int) Math.ceil(3.141592653589793 / Math.atan(1.0 / radius / 2.0)),
             phi_n = 0;
             phi_n < 2 * steps;
             ++phi_n) {
            for (int theta_n = 0; theta_n < steps; ++theta_n) {
                final double phi = 6.283185307179586 / steps * phi_n;
                final double theta = 3.141592653589793 / steps * theta_n;
                final Vector3 point = new Vector3(
                                          Math.sin(theta) * Math.cos(phi),
                                          Math.cos(theta),
                                          Math.sin(theta) * Math.sin(phi)
                )
                                          .multiply(radius);
                fieldBlocks.add(point);
            }
        }
        return fieldBlocks;
    }

    @Override
    public Set<Vector3> getInteriorPoints(final IFieldInteraction projector) {
        final Set<Vector3> fieldBlocks = new HashSet<>();
        final Vector3 translation = projector.getTranslation();
        for (int radius = projector.getModuleCount(
                     ModularForceFieldSystem.itemModuleScale, new int[0]
                 ),
                 x = -radius;
             x <= radius;
             ++x) {
            for (int z = -radius; z <= radius; ++z) {
                for (int y = -radius; y <= radius; ++y) {
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
        return new Vector3((TileEntity) projector)
                   .add(projector.getTranslation())
                   .distanceTo(position)
            < projector.getModuleCount(
                ModularForceFieldSystem.itemModuleScale, new int[0]
            );
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void render(
        final IProjector projector,
        final double x1,
        final double y1,
        final double z1,
        final float f,
        final long ticks
    ) {
        final float scale = 0.15f;
        GL11.glScalef(scale, scale, scale);
        final float radius = 1.5f;
        for (int steps
             = (int) Math.ceil(3.141592653589793 / Math.atan(1.0 / radius / 2.0)),
             phi_n = 0;
             phi_n < 2 * steps;
             ++phi_n) {
            for (int theta_n = 0; theta_n < steps; ++theta_n) {
                final double phi = 6.283185307179586 / steps * phi_n;
                final double theta = 3.141592653589793 / steps * theta_n;
                final Vector3 vector = new Vector3(
                    Math.sin(theta) * Math.cos(phi),
                    Math.cos(theta),
                    Math.sin(theta) * Math.sin(phi)
                );
                vector.multiply(radius);
                GL11.glTranslated(vector.x, vector.y, vector.z);
                ModelCube.INSTNACE.render();
                GL11.glTranslated(-vector.x, -vector.y, -vector.z);
            }
        }
    }
}
