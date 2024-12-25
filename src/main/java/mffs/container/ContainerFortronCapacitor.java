package mffs.container;

import mffs.base.ContainerBase;
import mffs.slot.SlotBase;
import mffs.slot.SlotCard;
import mffs.tileentity.TileEntityFortronCapacitor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainerFortronCapacitor extends ContainerBase {
    private TileEntityFortronCapacitor tileEntity;

    public ContainerFortronCapacitor(
        final EntityPlayer player, final TileEntityFortronCapacitor tileEntity
    ) {
        super((IInventory) tileEntity);
        this.tileEntity = tileEntity;
        this.addSlotToContainer((Slot) new SlotCard(this.tileEntity, 0, 9, 74));
        this.addSlotToContainer((Slot) new SlotCard(this.tileEntity, 1, 27, 74));
        this.addSlotToContainer((Slot) new SlotBase(this.tileEntity, 2, 154, 47));
        this.addSlotToContainer((Slot) new SlotBase(this.tileEntity, 3, 154, 67));
        this.addSlotToContainer((Slot) new SlotBase(this.tileEntity, 4, 154, 87));
        this.addPlayerInventory(player);
    }
}
