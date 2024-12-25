package mffs.container;

import mffs.base.ContainerBase;
import mffs.slot.SlotActive;
import mffs.slot.SlotBase;
import mffs.tileentity.TileEntityBiometricIdentifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainerBiometricIdentifier extends ContainerBase {
    public ContainerBiometricIdentifier(
        final EntityPlayer player, final TileEntityBiometricIdentifier tileentity
    ) {
        super((IInventory) tileentity);
        this.addSlotToContainer((Slot) new SlotActive(tileentity, 0, 88, 91));
        this.addSlotToContainer((Slot) new SlotBase(tileentity, 1, 8, 46));
        this.addSlotToContainer((Slot) new SlotActive(tileentity, 2, 8, 91));
        for (int var4 = 0; var4 < 9; ++var4) {
            this.addSlotToContainer((Slot
            ) new SlotActive(tileentity, 3 + var4, 8 + var4 * 18, 111));
        }
        this.addSlotToContainer((Slot) new SlotBase(tileentity, 12, 8, 66));
        this.addPlayerInventory(player);
    }
}
