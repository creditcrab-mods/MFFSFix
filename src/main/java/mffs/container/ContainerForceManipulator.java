package mffs.container;

import mffs.base.ContainerBase;
import mffs.slot.SlotBase;
import mffs.slot.SlotCard;
import mffs.tileentity.TileEntityForceManipulator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainerForceManipulator extends ContainerBase {
    public ContainerForceManipulator(
        final EntityPlayer player, final TileEntityForceManipulator tileEntity
    ) {
        super((IInventory) tileEntity);
        this.addSlotToContainer((Slot) new SlotCard(tileEntity, 0, 73, 91));
        this.addSlotToContainer((Slot) new SlotCard(tileEntity, 1, 91, 91));
        this.addSlotToContainer((Slot) new SlotBase(tileEntity, 2, 118, 45));
        int i = 3;
        for (int xSlot = 0; xSlot < 4; ++xSlot) {
            for (int ySlot = 0; ySlot < 4; ++ySlot) {
                if ((xSlot != 1 || ySlot != 1) && (xSlot != 2 || ySlot != 2)
                    && (xSlot != 1 || ySlot != 2) && (xSlot != 2 || ySlot != 1)) {
                    this.addSlotToContainer((Slot
                    ) new SlotBase(tileEntity, i, 91 + 18 * xSlot, 18 + 18 * ySlot));
                    ++i;
                }
            }
        }
        for (int xSlot = 0; xSlot < 3; ++xSlot) {
            for (int ySlot = 0; ySlot < 2; ++ySlot) {
                this.addSlotToContainer((Slot
                ) new SlotBase(tileEntity, i, 31 + 18 * xSlot, 19 + 18 * ySlot));
                ++i;
            }
        }
        this.addPlayerInventory(player);
    }
}
