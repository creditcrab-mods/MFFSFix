package calclavia.lib.gui;

import calclavia.lib.IPlayerUsing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerBase extends Container {
    protected int slotCount = 0;
    private IInventory inventory;

    public ContainerBase(IInventory inventory) {
        this.inventory = inventory;
        this.slotCount = inventory.getSizeInventory();
    }

    public void onContainerClosed(EntityPlayer player) {
        if (this.inventory instanceof IPlayerUsing) {
            ((IPlayerUsing) this.inventory).getPlayersUsing().remove(player);
        }
    }

    public void addPlayerInventory(EntityPlayer player) {
        if (this.inventory instanceof IPlayerUsing) {
            ((IPlayerUsing) this.inventory).getPlayersUsing().add(player);
        }

        int var3;
        for (var3 = 0; var3 < 3; ++var3) {
            for (int var4 = 0; var4 < 9; ++var4) {
                this.addSlotToContainer(new Slot(
                    player.inventory, var4 + var3 * 9 + 9, 8 + var4 * 18, 135 + var3 * 18
                ));
            }
        }

        for (var3 = 0; var3 < 9; ++var3) {
            this.addSlotToContainer(new Slot(player.inventory, var3, 8 + var3 * 18, 193));
        }
    }

    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slotID) {
        ItemStack var2 = null;
        Slot var3 = (Slot) super.inventorySlots.get(slotID);
        if (var3 != null && var3.getHasStack()) {
            ItemStack itemStack = var3.getStack();
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

    public boolean canInteractWith(EntityPlayer entityplayer) {
        return this.inventory.isUseableByPlayer(entityplayer);
    }
}
