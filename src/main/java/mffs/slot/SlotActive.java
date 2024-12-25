package mffs.slot;

import mffs.base.TileEntityInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class SlotActive extends SlotBase {
    public SlotActive(
        final TileEntityInventory tileEntity, final int id, final int par4, final int par5
    ) {
        super(tileEntity, id, par4, par5);
    }

    @Override
    public boolean isItemValid(final ItemStack itemStack) {
        return super.isItemValid(itemStack) && !super.tileEntity.isActive();
    }

    public boolean canTakeStack(final EntityPlayer par1EntityPlayer) {
        return !super.tileEntity.isActive();
    }
}
