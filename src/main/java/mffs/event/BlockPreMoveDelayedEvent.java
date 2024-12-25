package mffs.event;

import mffs.DelayedEvent;
import mffs.IDelayedEventHandler;
import mffs.ManipulatorHelper;
import mffs.api.ISpecialForceManipulation;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;

public class BlockPreMoveDelayedEvent extends DelayedEvent {
    private World world;
    private Vector3 position;
    private Vector3 newPosition;

    public BlockPreMoveDelayedEvent(
        final IDelayedEventHandler handler,
        final int ticks,
        final World world,
        final Vector3 position,
        final Vector3 newPosition
    ) {
        super(handler, ticks);
        this.world = world;
        this.position = position;
        this.newPosition = newPosition;
    }

    @Override
    protected void onEvent() {
        if (!this.world.isRemote) {
            final TileEntity tileEntity
                = this.position.getTileEntity((IBlockAccess) this.world);
            if (tileEntity instanceof ISpecialForceManipulation) {
                ((ISpecialForceManipulation) tileEntity)
                    .move(
                        this.newPosition.intX(),
                        this.newPosition.intY(),
                        this.newPosition.intZ()
                    );
            }
            final Block blockID = this.position.getBlock((IBlockAccess) this.world);
            final int blockMetadata
                = this.position.getBlockMetadata((IBlockAccess) this.world);
            final NBTTagCompound tileData = new NBTTagCompound();
            if (tileEntity != null) {
                tileEntity.writeToNBT(tileData);
            }
            ManipulatorHelper.setBlockSneaky(
                this.world, this.position, Blocks.air, 0, null
            );
            super.handler.getQuedDelayedEvents().add(new BlockPostMoveDelayedEvent(
                super.handler,
                0,
                this.world,
                this.position,
                this.newPosition,
                blockID,
                blockMetadata,
                tileEntity,
                tileData
            ));
        }
    }
}
