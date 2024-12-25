package mffs.item.module.projector;

import java.util.Set;

import mffs.api.IProjector;
import mffs.item.module.ItemModule;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidBlock;
import universalelectricity.core.vector.Vector3;

public class ItemModuleSponge extends ItemModule {
    public ItemModuleSponge() {
        super("moduleSponge");
        this.setMaxStackSize(1);
    }

    @Override
    public boolean onProject(final IProjector projector, final Set<Vector3> fields) {
        if (projector.getTicks() % 60L == 0L) {
            final World world = ((TileEntity) projector).getWorldObj();
            for (final Vector3 point : projector.getInteriorPoints()) {
                if (point.getBlock(world) instanceof IFluidBlock
                    || point.getBlock(world) instanceof BlockLiquid) {
                    point.setBlock(world, Blocks.air);
                }
            }
        }
        return super.onProject(projector, fields);
    }
}
