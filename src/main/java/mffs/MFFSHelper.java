package mffs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import calclavia.lib.CalculationHelper;
import icbm.api.IBlockFrequency;
import mffs.api.IProjector;
import mffs.api.fortron.IFortronFrequency;
import mffs.api.modules.IModuleAcceptor;
import mffs.api.security.IInterdictionMatrix;
import mffs.api.security.Permission;
import mffs.fortron.FrequencyGrid;
import mffs.item.module.projector.ItemModeCustom;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import universalelectricity.core.vector.Vector3;

public class MFFSHelper {
    public static void transferFortron(
        final IFortronFrequency transferer,
        final Set<IFortronFrequency> frequencyTiles,
        final TransferMode transferMode,
        final int limit
    ) {
        if (transferer != null && frequencyTiles.size() > 1) {
            int totalFortron = 0;
            int totalCapacity = 0;
            for (final IFortronFrequency machine : frequencyTiles) {
                if (machine != null) {
                    totalFortron += machine.getFortronEnergy();
                    totalCapacity += machine.getFortronCapacity();
                }
            }
            if (totalFortron > 0 && totalCapacity > 0) {
                switch (transferMode) {
                    case EQUALIZE: {
                        for (final IFortronFrequency machine : frequencyTiles) {
                            if (machine != null) {
                                final double capacityPercentage
                                    = machine.getFortronCapacity()
                                    / (double) totalCapacity;
                                final int amountToSet
                                    = (int) (totalFortron * capacityPercentage);
                                doTransferFortron(
                                    transferer,
                                    machine,
                                    amountToSet - machine.getFortronEnergy(),
                                    limit
                                );
                            }
                        }
                        break;
                    }
                    case DISTRIBUTE: {
                        final int amountToSet2 = totalFortron / frequencyTiles.size();
                        for (final IFortronFrequency machine2 : frequencyTiles) {
                            if (machine2 != null) {
                                doTransferFortron(
                                    transferer,
                                    machine2,
                                    amountToSet2 - machine2.getFortronEnergy(),
                                    limit
                                );
                            }
                        }
                        break;
                    }
                    case DRAIN: {
                        frequencyTiles.remove(transferer);
                        for (final IFortronFrequency machine : frequencyTiles) {
                            if (machine != null) {
                                final double capacityPercentage
                                    = machine.getFortronCapacity()
                                    / (double) totalCapacity;
                                final int amountToSet
                                    = (int) (totalFortron * capacityPercentage);
                                if (amountToSet - machine.getFortronEnergy() <= 0) {
                                    continue;
                                }
                                doTransferFortron(
                                    transferer,
                                    machine,
                                    amountToSet - machine.getFortronEnergy(),
                                    limit
                                );
                            }
                        }
                        break;
                    }
                    case FILL: {
                        if (transferer.getFortronEnergy()
                            < transferer.getFortronCapacity()) {
                            frequencyTiles.remove(transferer);
                            final int requiredFortron = transferer.getFortronCapacity()
                                - transferer.getFortronEnergy();
                            for (final IFortronFrequency machine2 : frequencyTiles) {
                                if (machine2 != null) {
                                    final int amountToConsume = Math.min(
                                        requiredFortron, machine2.getFortronEnergy()
                                    );
                                    final int amountToSet
                                        = -machine2.getFortronEnergy() - amountToConsume;
                                    if (amountToConsume <= 0) {
                                        continue;
                                    }
                                    doTransferFortron(
                                        transferer,
                                        machine2,
                                        amountToSet - machine2.getFortronEnergy(),
                                        limit
                                    );
                                }
                            }
                            break;
                        }
                        break;
                    }
                }
            }
        }
    }

    public static void doTransferFortron(
        final IFortronFrequency transferer,
        final IFortronFrequency receiver,
        int joules,
        final int limit
    ) {
        if (transferer != null && receiver != null) {
            final TileEntity tileEntity = (TileEntity) transferer;
            final World world = tileEntity.getWorldObj();
            boolean isCamo = false;
            if (transferer instanceof IModuleAcceptor) {
                isCamo
                    = (((IModuleAcceptor) transferer)
                           .getModuleCount(
                               ModularForceFieldSystem.itemModuleCamouflage, new int[0]
                           )
                       > 0);
            }
            if (joules > 0) {
                joules = Math.min(joules, limit);
                int toBeInjected = receiver.provideFortron(
                    transferer.requestFortron(joules, false), false
                );
                toBeInjected = transferer.requestFortron(
                    receiver.provideFortron(toBeInjected, true), true
                );
                if (world.isRemote && toBeInjected > 0 && !isCamo) {
                    ModularForceFieldSystem.proxy.renderBeam(
                        world,
                        Vector3.add(new Vector3(tileEntity), 0.5),
                        Vector3.add(new Vector3((TileEntity) receiver), 0.5),
                        0.6f,
                        0.6f,
                        1.0f,
                        20
                    );
                }
            } else {
                joules = Math.min(Math.abs(joules), limit);
                int toBeEjected = transferer.provideFortron(
                    receiver.requestFortron(joules, false), false
                );
                toBeEjected = receiver.requestFortron(
                    transferer.provideFortron(toBeEjected, true), true
                );
                if (world.isRemote && toBeEjected > 0 && !isCamo) {
                    ModularForceFieldSystem.proxy.renderBeam(
                        world,
                        Vector3.add(new Vector3((TileEntity) receiver), 0.5),
                        Vector3.add(new Vector3(tileEntity), 0.5),
                        0.6f,
                        0.6f,
                        1.0f,
                        20
                    );
                }
            }
        }
    }

    public static IInterdictionMatrix
    getNearestInterdictionMatrix(final World world, final Vector3 position) {
        for (final IBlockFrequency frequencyTile : FrequencyGrid.instance().get()) {
            if (((TileEntity) frequencyTile).getWorldObj() == world
                && frequencyTile instanceof IInterdictionMatrix) {
                final IInterdictionMatrix interdictionMatrix
                    = (IInterdictionMatrix) frequencyTile;
                if (interdictionMatrix.isActive()
                    && position.distanceTo(new Vector3((TileEntity) interdictionMatrix))
                        <= interdictionMatrix.getActionRange()) {
                    return interdictionMatrix;
                }
                continue;
            }
        }
        return null;
    }

    public static boolean isPermittedByInterdictionMatrix(
        final IInterdictionMatrix interdictionMatrix,
        final String username,
        final Permission... permissions
    ) {
        if (interdictionMatrix != null && interdictionMatrix.isActive()
            && interdictionMatrix.getBiometricIdentifier() != null) {
            for (final Permission permission : permissions) {
                if (!interdictionMatrix.getBiometricIdentifier().isAccessGranted(
                        username, permission
                    )) {
                    return interdictionMatrix.getModuleCount(
                               ModularForceFieldSystem.itemModuleInvert, new int[0]
                           )
                        > 0;
                }
            }
        }
        return interdictionMatrix.getModuleCount(
                   ModularForceFieldSystem.itemModuleInvert, new int[0]
               )
            <= 0;
    }

    public static List<String>
    splitStringPerWord(final String string, final int wordsPerLine) {
        final String[] words = string.split(" ");
        final List<String> lines = new ArrayList<>();
        for (int lineCount = 0;
             lineCount < Math.ceil(words.length / (float) wordsPerLine);
             ++lineCount) {
            String stringInLine = "";
            for (int i = lineCount * wordsPerLine;
                 i < Math.min(wordsPerLine + lineCount * wordsPerLine, words.length);
                 ++i) {
                stringInLine = stringInLine + words[i] + " ";
            }
            lines.add(stringInLine.trim());
        }
        return lines;
    }

    public static ItemStack
    getFirstItemBlock(final TileEntity tileEntity, final ItemStack itemStack) {
        return getFirstItemBlock(tileEntity, itemStack, true);
    }

    public static ItemStack getFirstItemBlock(
        final TileEntity tileEntity, final ItemStack itemStack, final boolean recur
    ) {
        if (tileEntity instanceof IProjector) {
            for (final int i : ((IProjector) tileEntity).getModuleSlots()) {
                final ItemStack checkStack
                    = getFirstItemBlock(i, (IInventory) tileEntity, itemStack);
                if (checkStack != null) {
                    return checkStack;
                }
            }
        } else if (tileEntity instanceof IInventory) {
            final IInventory inventory = (IInventory) tileEntity;
            for (int j = 0; j < inventory.getSizeInventory(); ++j) {
                final ItemStack checkStack2 = getFirstItemBlock(j, inventory, itemStack);
                if (checkStack2 != null) {
                    return checkStack2;
                }
            }
        }
        if (recur) {
            for (int k = 0; k < 6; ++k) {
                final ForgeDirection direction = ForgeDirection.getOrientation(k);
                final Vector3 vector = new Vector3(tileEntity);
                vector.modifyPositionFromSide(direction);
                final TileEntity checkTile
                    = vector.getTileEntity((IBlockAccess) tileEntity.getWorldObj());
                if (checkTile != null) {
                    final ItemStack checkStack
                        = getFirstItemBlock(checkTile, itemStack, false);
                    if (checkStack != null) {
                        return checkStack;
                    }
                }
            }
        }
        return null;
    }

    public static ItemStack getFirstItemBlock(
        final int i, final IInventory inventory, final ItemStack itemStack
    ) {
        final ItemStack checkStack = inventory.getStackInSlot(i);
        if (checkStack != null && checkStack.getItem() instanceof ItemBlock
            && (itemStack == null || checkStack.isItemEqual(itemStack))) {
            return checkStack;
        }
        return null;
    }

    public static Block getFilterBlock(final ItemStack itemStack) {
        if (itemStack != null && itemStack.getItem() instanceof ItemBlock) {
            final Block block = Block.getBlockFromItem(itemStack.getItem());
            if (block.renderAsNormalBlock()) {
                return block;
            }
        }
        return null;
    }

    public static ItemStack
    getCamoBlock(final IProjector projector, final Vector3 position) {
        if (projector != null && !((TileEntity) projector).getWorldObj().isRemote
            && projector != null
            && projector.getModuleCount(
                   ModularForceFieldSystem.itemModuleCamouflage, new int[0]
               ) > 0) {
            if (projector.getMode() instanceof ItemModeCustom) {
                final HashMap<Vector3, int[]> fieldMap
                    = ((ItemModeCustom) projector.getMode())
                          .getFieldBlockMap(projector, projector.getModeStack());
                if (fieldMap != null) {
                    final Vector3 fieldCenter = new Vector3((TileEntity) projector)
                                                    .add(projector.getTranslation());
                    final Vector3 relativePosition
                        = position.clone().subtract(fieldCenter);
                    CalculationHelper.rotateByAngle(
                        relativePosition,
                        -projector.getRotationYaw(),
                        -projector.getRotationPitch()
                    );
                    final int[] blockInfo = fieldMap.get(relativePosition.round());
                    if (blockInfo != null && blockInfo[0] > 0) {
                        return new ItemStack(
                            Block.getBlockById(blockInfo[0]), 1, blockInfo[1]
                        );
                    }
                }
            }
            for (final int i : projector.getModuleSlots()) {
                final ItemStack checkStack = projector.getStackInSlot(i);
                final Block block = getFilterBlock(checkStack);
                if (block != null) {
                    return checkStack;
                }
            }
        }
        return null;
    }

    public static NBTTagCompound getNBTTagCompound(final ItemStack itemStack) {
        if (itemStack != null) {
            if (itemStack.getTagCompound() == null) {
                itemStack.setTagCompound(new NBTTagCompound());
            }
            return itemStack.getTagCompound();
        }
        return null;
    }

    public static boolean hasPermission(
        final World world,
        final Vector3 position,
        final Permission permission,
        final EntityPlayer player
    ) {
        final IInterdictionMatrix interdictionMatrix
            = getNearestInterdictionMatrix(world, position);
        return interdictionMatrix == null
            || isPermittedByInterdictionMatrix(
                   interdictionMatrix, player.getDisplayName(), permission
            );
    }

    public static boolean hasPermission(
        final World world,
        final Vector3 position,
        final PlayerInteractEvent.Action action,
        final EntityPlayer player
    ) {
        final IInterdictionMatrix interdictionMatrix
            = getNearestInterdictionMatrix(world, position);
        return interdictionMatrix == null
            || hasPermission(world, position, interdictionMatrix, action, player);
    }

    public static boolean hasPermission(
        final World world,
        final Vector3 position,
        final IInterdictionMatrix interdictionMatrix,
        final PlayerInteractEvent.Action action,
        final EntityPlayer player
    ) {
        boolean hasPermission = true;
        if (action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK
            && position.getTileEntity((IBlockAccess) world) != null
            && interdictionMatrix.getModuleCount(
                   ModularForceFieldSystem.itemModuleBlockAccess, new int[0]
               ) > 0) {
            hasPermission = false;
            if (isPermittedByInterdictionMatrix(
                    interdictionMatrix, player.getDisplayName(), Permission.BLOCK_ACCESS
                )) {
                hasPermission = true;
            }
        }
        if (hasPermission
            && interdictionMatrix.getModuleCount(
                   ModularForceFieldSystem.itemModuleBlockAlter, new int[0]
               ) > 0
            && (player.getCurrentEquippedItem() != null
                || action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK)) {
            hasPermission = false;
            if (isPermittedByInterdictionMatrix(
                    interdictionMatrix, player.getDisplayName(), Permission.BLOCK_ALTER
                )) {
                hasPermission = true;
            }
        }
        return hasPermission;
    }
}
