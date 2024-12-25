package mffs.item.module.projector;

import java.util.HashSet;
import java.util.Set;

import mffs.IDelayedEventHandler;
import mffs.MFFSHelper;
import mffs.ModularForceFieldSystem;
import mffs.api.Blacklist;
import mffs.api.IProjector;
import mffs.base.PacketFxs;
import mffs.base.TileEntityInventory;
import mffs.event.BlockDropDelayedEvent;
import mffs.event.BlockInventoryDropDelayedEvent;
import mffs.item.module.ItemModule;
import mffs.tileentity.TileEntityForceFieldProjector;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.IFluidBlock;
import universalelectricity.core.vector.Vector3;

public class ItemModuleDisintegration extends ItemModule {
    private int blockCount;

    public ItemModuleDisintegration() {
        super("moduleDisintegration");
        this.blockCount = 0;
        this.setMaxStackSize(1);
        this.setCost(20.0f);
    }

    @Override
    public boolean onProject(final IProjector projector, final Set<Vector3> fields) {
        this.blockCount = 0;
        return false;
    }

    @Override
    public int onProject(final IProjector projector, final Vector3 position) {
        if (projector.getTicks() % 40L == 0L) {
            final TileEntity tileEntity = (TileEntity) projector;
            final Block block
                = position.getBlock((IBlockAccess) tileEntity.getWorldObj());
            if (block != Blocks.air) {
                if (projector.getModuleCount(
                        ModularForceFieldSystem.itemModuleCamouflage, new int[0]
                    )
                    > 0) {
                    final int blockMetadata = position.getBlockMetadata((IBlockAccess
                    ) tileEntity.getWorldObj());
                    final Set<ItemStack> filterStacks = new HashSet<>();
                    for (final int i : projector.getModuleSlots()) {
                        final ItemStack checkStack = projector.getStackInSlot(i);
                        final Block filterBlock = MFFSHelper.getFilterBlock(checkStack);
                        if (filterBlock != null) {
                            filterStacks.add(checkStack);
                        }
                    }
                    boolean contains = false;
                    for (final ItemStack filterStack : filterStacks) {
                        if (filterStack.isItemEqual(new ItemStack(block, 1, blockMetadata)
                            )) {
                            contains = true;
                            break;
                        }
                    }
                    if (!contains) {
                        return 1;
                    }
                }
                if (Blacklist.disintegrationBlacklist.contains(block)
                    || block instanceof IFluidBlock) {
                    return 1;
                }

                NBTTagCompound fxsData = new NBTTagCompound();

                position.writeToNBT(fxsData);
                fxsData.setInteger("type", 2);

                ModularForceFieldSystem.channel.sendToAll(
                    new PacketFxs(new Vector3((TileEntity) projector), fxsData)
                );

                if (projector.getModuleCount(
                        ModularForceFieldSystem.itemModuleCollection, new int[0]
                    )
                    > 0) {
                    ((TileEntityForceFieldProjector) projector)
                        .getDelayedEvents()
                        .add(new BlockInventoryDropDelayedEvent(
                            (IDelayedEventHandler) projector,
                            39,
                            block,
                            tileEntity.getWorldObj(),
                            position,
                            (TileEntityInventory) projector
                        ));
                } else {
                    ((TileEntityForceFieldProjector) projector)
                        .getDelayedEvents()
                        .add(new BlockDropDelayedEvent(
                            (IDelayedEventHandler) projector,
                            39,
                            block,
                            tileEntity.getWorldObj(),
                            position
                        ));
                }
                if (this.blockCount++
                    >= projector.getModuleCount(
                           ModularForceFieldSystem.itemModuleSpeed, new int[0]
                       ) / 3) {
                    return 2;
                }
                return 1;
            }
        }
        return 1;
    }
}
