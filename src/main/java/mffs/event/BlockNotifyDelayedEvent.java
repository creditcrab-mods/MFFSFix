package mffs.event;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.ReflectionHelper;
import mffs.DelayedEvent;
import mffs.IDelayedEventHandler;
import mffs.api.ISpecialForceManipulation;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;

public class BlockNotifyDelayedEvent extends DelayedEvent {
    private World world;
    private Vector3 position;

    public BlockNotifyDelayedEvent(
        final IDelayedEventHandler handler,
        final int ticks,
        final World world,
        final Vector3 position
    ) {
        super(handler, ticks);
        this.world = world;
        this.position = position;
    }

    @Override
    protected void onEvent() {
        if (!this.world.isRemote) {
            this.world.notifyBlocksOfNeighborChange(
                this.position.intX(),
                this.position.intY(),
                this.position.intZ(),
                this.position.getBlock((IBlockAccess) this.world)
            );
            final TileEntity newTile
                = this.position.getTileEntity((IBlockAccess) this.world);
            if (newTile != null) {
                if (newTile instanceof ISpecialForceManipulation) {
                    ((ISpecialForceManipulation) newTile).postMove();
                }
                if (Loader.isModLoaded("BuildCraft|Factory")) {
                    try {
                        final Class clazz
                            = Class.forName("buildcraft.factory.TileQuarry");
                        if (clazz == newTile.getClass()) {
                            // TODO: W T F AAAAAAAAAAAAA
                            ReflectionHelper.setPrivateValue(
                                clazz,
                                (Object) newTile,
                                (Object) true,
                                new String[] { "isAlive" }
                            );
                        }
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
