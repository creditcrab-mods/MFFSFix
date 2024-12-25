package mffs.item.module.projector;

import java.util.HashMap;
import java.util.Set;

import calclavia.lib.CalculationHelper;
import mffs.ModularForceFieldSystem;
import mffs.api.Blacklist;
import mffs.api.IProjector;
import mffs.base.PacketFxs;
import mffs.item.module.ItemModule;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidBlock;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;

public class ItemModuleStablize extends ItemModule {
    private int blockCount;

    public ItemModuleStablize() {
        super("moduleStabilize");
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
        int[] blockInfo = null;
        if (projector.getTicks() % 40L == 0L) {
            if (projector.getMode() instanceof ItemModeCustom) {
                final HashMap<Vector3, int[]> fieldBlocks
                    = ((ItemModeCustom) projector.getMode())
                          .getFieldBlockMap(projector, projector.getModeStack());
                final Vector3 fieldCenter
                    = new Vector3((TileEntity) projector).add(projector.getTranslation());
                final Vector3 relativePosition = position.clone().subtract(fieldCenter);
                CalculationHelper.rotateByAngle(
                    relativePosition,
                    -projector.getRotationYaw(),
                    -projector.getRotationPitch()
                );
                blockInfo = fieldBlocks.get(relativePosition.round());
            }
            for (int dir = 0; dir < 6; ++dir) {
                final ForgeDirection direction = ForgeDirection.getOrientation(dir);
                final TileEntity tileEntity = VectorHelper.getTileEntityFromSide(
                    ((TileEntity) projector).getWorldObj(),
                    new Vector3((TileEntity) projector),
                    direction
                );
                if (tileEntity instanceof IInventory) {
                    final IInventory inventory = (IInventory) tileEntity;
                    for (int i = 0; i < inventory.getSizeInventory(); ++i) {
                        final ItemStack checkStack = inventory.getStackInSlot(i);
                        if (checkStack != null
                            && checkStack.getItem() instanceof ItemBlock) {
                            if (blockInfo != null) {
                                if (Block.getBlockById(blockInfo[0])
                                    != Block.getBlockFromItem((ItemBlock
                                    ) checkStack.getItem())) {
                                    continue;
                                }
                            }
                            try {
                                if (((TileEntity) projector)
                                        .getWorldObj()
                                        .canPlaceEntityOnSide(
                                            Block.getBlockFromItem((ItemBlock
                                            ) checkStack.getItem()),
                                            position.intX(),
                                            position.intY(),
                                            position.intZ(),
                                            false,
                                            0,
                                            (Entity) null,
                                            checkStack
                                        )) {
                                    final int metadata = (blockInfo != null)
                                        ? blockInfo[1]
                                        : (checkStack.getHasSubtypes()
                                               ? checkStack.getItemDamage()
                                               : 0);
                                    final Block block = (blockInfo != null)
                                        ? Block.getBlockById(blockInfo[0])
                                        : null;
                                    if (Blacklist.stabilizationBlacklist.contains(block)
                                        || block instanceof IFluidBlock) {
                                        return 1;
                                    }
                                    ((ItemBlock) checkStack.getItem())
                                        .placeBlockAt(
                                            checkStack,
                                            (EntityPlayer) null,
                                            ((TileEntity) projector).getWorldObj(),
                                            position.intX(),
                                            position.intY(),
                                            position.intZ(),
                                            0,
                                            0.0f,
                                            0.0f,
                                            0.0f,
                                            metadata
                                        );
                                    inventory.decrStackSize(i, 1);

                                    NBTTagCompound fxsData = new NBTTagCompound();

                                    position.writeToNBT(fxsData);
                                    fxsData.setInteger("type", 1);

                                    ModularForceFieldSystem.channel.sendToAll(
                                        new PacketFxs(
                                            new Vector3((TileEntity) projector), fxsData
                                        )
                                    );

                                    if (this.blockCount++
                                        >= projector.getModuleCount(
                                               ModularForceFieldSystem.itemModuleSpeed,
                                               new int[0]
                                           ) / 3) {
                                        return 2;
                                    }
                                    return 1;
                                }
                            } catch (final Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        return 1;
    }
}
