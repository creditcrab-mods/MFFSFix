package mffs.container;

import mffs.base.ContainerBase;
import mffs.slot.SlotBase;
import mffs.slot.SlotCard;
import mffs.tileentity.TileEntityInterdictionMatrix;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainerInterdictionMatrix extends ContainerBase {
    public ContainerInterdictionMatrix(
        final EntityPlayer player, final TileEntityInterdictionMatrix tileEntity
    ) {
        super((IInventory) tileEntity);
        this.addSlotToContainer((Slot) new SlotCard(tileEntity, 0, 87, 89));
        this.addSlotToContainer((Slot) new SlotBase(tileEntity, 1, 69, 89));
        for (int var3 = 0; var3 < 2; ++var3) {
            for (int var4 = 0; var4 < 4; ++var4) {
                this.addSlotToContainer((Slot) new SlotBase(
                    tileEntity, var4 + var3 * 4 + 2, 99 + var4 * 18, 31 + var3 * 18
                ));
            }
        }
        for (int var5 = 0; var5 < 9; ++var5) {
            this.addSlotToContainer((Slot
            ) new SlotBase(tileEntity, var5 + 8 + 2, 9 + var5 * 18, 69));
        }
        this.addPlayerInventory(player);
    }
}
