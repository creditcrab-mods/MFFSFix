package mffs.event;

import java.util.ArrayList;

import mffs.IDelayedEventHandler;
import mffs.base.TileEntityInventory;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;

public class BlockInventoryDropDelayedEvent extends BlockDropDelayedEvent {
    private TileEntityInventory projector;

    public BlockInventoryDropDelayedEvent(
        final IDelayedEventHandler handler,
        final int ticks,
        final Block block,
        final World world,
        final Vector3 position,
        final TileEntityInventory projector
    ) {
        super(handler, ticks, block, world, position);
        this.projector = projector;
    }

    @Override
    protected void onEvent() {
        if (super.position.getBlock((IBlockAccess) super.world) == super.block) {
            final ArrayList<ItemStack> itemStacks = super.block.getDrops(
                super.world,
                super.position.intX(),
                super.position.intY(),
                super.position.intZ(),
                super.position.getBlockMetadata((IBlockAccess) super.world),
                0
            );
            for (final ItemStack itemStack : itemStacks) {
                this.projector.mergeIntoInventory(itemStack);
            }
            super.position.setBlock(super.world, Blocks.air);
        }
    }
}
