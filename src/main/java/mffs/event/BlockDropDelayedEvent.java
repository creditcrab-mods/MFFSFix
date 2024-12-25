package mffs.event;

import mffs.DelayedEvent;
import mffs.IDelayedEventHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;

public class BlockDropDelayedEvent extends DelayedEvent {
    protected Block block;
    protected World world;
    protected Vector3 position;

    public BlockDropDelayedEvent(
        final IDelayedEventHandler handler,
        final int ticks,
        final Block block,
        final World world,
        final Vector3 position
    ) {
        super(handler, ticks);
        this.block = block;
        this.world = world;
        this.position = position;
    }

    @Override
    protected void onEvent() {
        if (this.position.getBlock((IBlockAccess) this.world) == this.block) {
            this.block.dropBlockAsItem(
                this.world,
                this.position.intX(),
                this.position.intY(),
                this.position.intZ(),
                this.position.getBlockMetadata((IBlockAccess) this.world),
                0
            );
            this.position.setBlock(this.world, Blocks.air);
        }
    }
}
