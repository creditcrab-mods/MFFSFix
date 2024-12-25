package mffs.base;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerBase extends Container {
    protected int slotCount;
    private IInventory inventory;

    public ContainerBase(final IInventory inventory) {
        this.slotCount = 0;
        this.inventory = inventory;
        this.slotCount = inventory.getSizeInventory();
    }

    @Override
    public void onContainerClosed(final EntityPlayer player) {
        if (this.inventory instanceof TileEntityBase) {
            ((TileEntityBase) this.inventory).playersUsing.remove(player);
        }
    }

    public void addPlayerInventory(final EntityPlayer player) {
        if (this.inventory instanceof TileEntityBase) {
            ((TileEntityBase) this.inventory).playersUsing.add(player);
        }
        for (int var3 = 0; var3 < 3; ++var3) {
            for (int var4 = 0; var4 < 9; ++var4) {
                this.addSlotToContainer(new Slot(
                    (IInventory) player.inventory,
                    var4 + var3 * 9 + 9,
                    8 + var4 * 18,
                    135 + var3 * 18
                ));
            }
        }
        for (int var3 = 0; var3 < 9; ++var3) {
            this.addSlotToContainer(
                new Slot((IInventory) player.inventory, var3, 8 + var3 * 18, 193)
            );
        }
    }

    @Override
    public ItemStack
    transferStackInSlot(final EntityPlayer par1EntityPlayer, final int slotID) {
        ItemStack var2 = null;
        final Slot var3 = (Slot) super.inventorySlots.get(slotID);
        if (var3 != null && var3.getHasStack()) {
            final ItemStack itemStack = var3.getStack();
            var2 = itemStack.copy();
            if (slotID >= this.slotCount) {
                boolean didTry = false;
                for (int i = 0; i < this.slotCount; ++i) {
                    if (this.getSlot(i).isItemValid(itemStack)) {
                        didTry = true;
                        if (this.mergeItemStack(itemStack, i, i + 1, false)) {
                            break;
                        }
                    }
                }
                if (!didTry) {
                    if (slotID < 27 + this.slotCount) {
                        if (!this.mergeItemStack(
                                itemStack, 27 + this.slotCount, 36 + this.slotCount, false
                            )) {
                            return null;
                        }
                    } else if (slotID >= 27 + this.slotCount && slotID < 36 + this.slotCount && !this.mergeItemStack(itemStack, this.slotCount, 27 + this.slotCount, false)) {
                        return null;
                    }
                }
            } else if (!this.mergeItemStack(
                           itemStack, this.slotCount, 36 + this.slotCount, false
                       )) {
                return null;
            }
            if (itemStack.stackSize == 0) {
                var3.putStack((ItemStack) null);
            } else {
                var3.onSlotChanged();
            }
            if (itemStack.stackSize == var2.stackSize) {
                return null;
            }
            var3.onPickupFromSlot(par1EntityPlayer, itemStack);
        }
        return var2;
    }

    @Override
    public boolean canInteractWith(final EntityPlayer entityplayer) {
        return this.inventory.isUseableByPlayer(entityplayer);
    }
}
