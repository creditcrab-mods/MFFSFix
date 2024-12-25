package mffs;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IGuiHandler;
import mffs.container.ContainerBiometricIdentifier;
import mffs.container.ContainerCoercionDeriver;
import mffs.container.ContainerForceFieldProjector;
import mffs.container.ContainerForceManipulator;
import mffs.container.ContainerFortronCapacitor;
import mffs.container.ContainerInterdictionMatrix;
import mffs.tileentity.TileEntityBiometricIdentifier;
import mffs.tileentity.TileEntityCoercionDeriver;
import mffs.tileentity.TileEntityForceFieldProjector;
import mffs.tileentity.TileEntityForceManipulator;
import mffs.tileentity.TileEntityFortronCapacitor;
import mffs.tileentity.TileEntityInterdictionMatrix;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;

public class CommonProxy implements IGuiHandler {
    public void preInit() {}

    public void init() {}

    public Object getClientGuiElement(
        final int ID,
        final EntityPlayer player,
        final World world,
        final int x,
        final int y,
        final int z
    ) {
        return null;
    }

    public Object getServerGuiElement(
        final int ID,
        final EntityPlayer player,
        final World world,
        final int x,
        final int y,
        final int z
    ) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity != null) {
            if (tileEntity.getClass() == TileEntityFortronCapacitor.class) {
                return new ContainerFortronCapacitor(
                    player, (TileEntityFortronCapacitor) tileEntity
                );
            }
            if (tileEntity.getClass() == TileEntityForceFieldProjector.class) {
                return new ContainerForceFieldProjector(
                    player, (TileEntityForceFieldProjector) tileEntity
                );
            }
            if (tileEntity.getClass() == TileEntityCoercionDeriver.class) {
                return new ContainerCoercionDeriver(
                    player, (TileEntityCoercionDeriver) tileEntity
                );
            }
            if (tileEntity.getClass() == TileEntityBiometricIdentifier.class) {
                return new ContainerBiometricIdentifier(
                    player, (TileEntityBiometricIdentifier) tileEntity
                );
            }
            if (tileEntity.getClass() == TileEntityInterdictionMatrix.class) {
                return new ContainerInterdictionMatrix(
                    player, (TileEntityInterdictionMatrix) tileEntity
                );
            }
            if (tileEntity.getClass() == TileEntityForceManipulator.class) {
                return new ContainerForceManipulator(
                    player, (TileEntityForceManipulator) tileEntity
                );
            }
        }
        return null;
    }

    public World getClientWorld() {
        return null;
    }

    public boolean isOp(final String username) {
        final MinecraftServer theServer
            = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (theServer == null)
            return false;

        for (String op : theServer.getConfigurationManager().func_152606_n())
            if (username.trim().equalsIgnoreCase(op))
                return true;

        return false;
    }

    public void renderBeam(
        final World world,
        final Vector3 position,
        final Vector3 target,
        final float red,
        final float green,
        final float blue,
        final int age
    ) {}

    public void renderHologram(
        final World world,
        final Vector3 position,
        final float red,
        final float green,
        final float blue,
        final int age,
        final Vector3 targetPosition
    ) {}

    public void renderHologramMoving(
        final World world,
        final Vector3 position,
        final float red,
        final float green,
        final float blue,
        final int age
    ) {}

    public boolean isSneaking() {
        return false;
    }
}
