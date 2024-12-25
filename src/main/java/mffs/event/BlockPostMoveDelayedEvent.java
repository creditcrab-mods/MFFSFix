package mffs.event;

import mffs.DelayedEvent;
import mffs.IDelayedEventHandler;
import mffs.ManipulatorHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;

public class BlockPostMoveDelayedEvent extends DelayedEvent {
    private World world;
    private Vector3 originalPosition;
    private Vector3 newPosition;
    private Block blockID;
    private int blockMetadata;
    private TileEntity tileEntity;
    private NBTTagCompound tileData;

    public BlockPostMoveDelayedEvent(
        final IDelayedEventHandler handler,
        final int ticks,
        final World world,
        final Vector3 originalPosition,
        final Vector3 newPosition,
        final Block blockID,
        final int blockMetadata,
        final TileEntity tileEntity,
        final NBTTagCompound tileData
    ) {
        super(handler, ticks);
        this.blockID = Blocks.air;
        this.blockMetadata = 0;
        this.world = world;
        this.originalPosition = originalPosition;
        this.newPosition = newPosition;
        this.blockID = blockID;
        this.blockMetadata = blockMetadata;
        this.tileEntity = tileEntity;
        this.tileData = tileData;
    }

    @Override
    protected void onEvent() {
        if (!this.world.isRemote && this.blockID != Blocks.air) {
            try {
                if (this.tileEntity != null && this.tileData != null) {
                    ManipulatorHelper.setBlockSneaky(
                        this.world,
                        this.newPosition,
                        this.blockID,
                        this.blockMetadata,
                        TileEntity.createAndLoadEntity(this.tileData)
                    );
                } else {
                    ManipulatorHelper.setBlockSneaky(
                        this.world,
                        this.newPosition,
                        this.blockID,
                        this.blockMetadata,
                        null
                    );
                }
                super.handler.getQuedDelayedEvents().add(new BlockNotifyDelayedEvent(
                    super.handler, 0, this.world, this.originalPosition
                ));
                super.handler.getQuedDelayedEvents().add(new BlockNotifyDelayedEvent(
                    super.handler, 0, this.world, this.newPosition
                ));
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }
}
