package mffs.item.mode;

import java.util.HashSet;
import java.util.Set;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mffs.api.IFieldInteraction;
import mffs.api.IProjector;
import mffs.render.model.ModelPlane;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;
import universalelectricity.core.vector.Vector3;

public class ItemModeTube extends ItemModeCube {
    public ItemModeTube() {
        super("modeTube");
    }

    @Override
    public Set<Vector3> getExteriorPoints(final IFieldInteraction projector) {
        final Set<Vector3> fieldBlocks = new HashSet<>();
        final ForgeDirection direction = projector.getDirection(
            (IBlockAccess) ((TileEntity) projector).getWorldObj(),
            ((TileEntity) projector).xCoord,
            ((TileEntity) projector).yCoord,
            ((TileEntity) projector).zCoord
        );
        final Vector3 posScale = projector.getPositiveScale();
        final Vector3 negScale = projector.getNegativeScale();
        for (float x = (float) (-negScale.intX()); x <= posScale.intX(); x += 0.5f) {
            for (float z = (float) (-negScale.intZ()); z <= posScale.intZ(); z += 0.5f) {
                for (float y = (float) (-negScale.intY()); y <= posScale.intY();
                     y += 0.5f) {
                    if (direction != ForgeDirection.UP && direction != ForgeDirection.DOWN
                        && (y == -negScale.intY() || y == posScale.intY())) {
                        fieldBlocks.add(new Vector3(x, y, z));
                    }
                    else if (direction != ForgeDirection.NORTH && direction != ForgeDirection.SOUTH && (z == -negScale.intZ() || z == posScale.intZ())) {
                        fieldBlocks.add(new Vector3(x, y, z));
                    }
                    else if (direction != ForgeDirection.WEST && direction != ForgeDirection.EAST && (x == -negScale.intX() || x == posScale.intX())) {
                        fieldBlocks.add(new Vector3(x, y, z));
                    }
                }
            }
        }
        return fieldBlocks;
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
        GL11.glTranslatef(-0.5f, 0.0f, 0.0f);
        ModelPlane.INSTNACE.render();
        GL11.glTranslatef(1.0f, 0.0f, 0.0f);
        ModelPlane.INSTNACE.render();
        GL11.glTranslatef(-0.5f, 0.0f, 0.0f);
        GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
        GL11.glTranslatef(0.5f, 0.0f, 0.0f);
        ModelPlane.INSTNACE.render();
        GL11.glTranslatef(-1.0f, 0.0f, 0.0f);
        ModelPlane.INSTNACE.render();
    }
}
