package mffs.slot;

import mffs.base.TileEntityInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotBase extends Slot {
    protected TileEntityInventory tileEntity;

    public SlotBase(
        final TileEntityInventory tileEntity, final int id, final int par4, final int par5
    ) {
        super((IInventory) tileEntity, id, par4, par5);
        this.tileEntity = tileEntity;
    }

    public boolean isItemValid(final ItemStack itemStack) {
        return this.tileEntity.isItemValidForSlot(super.slotNumber, itemStack);
    }

    public int getSlotStackLimit() {
        final ItemStack itemStack = this.tileEntity.getStackInSlot(super.slotNumber);
        if (itemStack != null) {
            return itemStack.getMaxStackSize();
        }
        return this.tileEntity.getInventoryStackLimit();
    }
}
