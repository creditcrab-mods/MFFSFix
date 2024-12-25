package mffs.container;

import mffs.base.ContainerBase;
import mffs.slot.SlotBase;
import mffs.slot.SlotCard;
import mffs.tileentity.TileEntityCoercionDeriver;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainerCoercionDeriver extends ContainerBase {
    public ContainerCoercionDeriver(
        final EntityPlayer player, final TileEntityCoercionDeriver tileEntity
    ) {
        super((IInventory) tileEntity);
        this.addSlotToContainer((Slot) new SlotCard(tileEntity, 0, 9, 41));
        this.addSlotToContainer((Slot) new SlotBase(tileEntity, 1, 9, 83));
        this.addSlotToContainer((Slot) new SlotBase(tileEntity, 2, 29, 83));
        this.addSlotToContainer((Slot) new SlotBase(tileEntity, 3, 154, 67));
        this.addSlotToContainer((Slot) new SlotBase(tileEntity, 4, 154, 87));
        this.addSlotToContainer((Slot) new SlotBase(tileEntity, 5, 154, 47));
        this.addPlayerInventory(player);
    }
}
