package mffs.tileentity;

import java.util.HashSet;
import java.util.Set;

import mffs.ModularForceFieldSystem;
import mffs.Settings;
import mffs.api.card.ICardIdentification;
import mffs.api.security.IBiometricIdentifier;
import mffs.api.security.Permission;
import mffs.base.PacketTile;
import mffs.base.TileEntityFrequency;
import mffs.item.card.ItemCardFrequency;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityBiometricIdentifier
    extends TileEntityFrequency implements IBiometricIdentifier {
    public static final int SLOT_COPY = 12;

    @Override
    public boolean isAccessGranted(final String username, final Permission permission) {
        if (!this.isActive()) {
            return true;
        }
        if (ModularForceFieldSystem.proxy.isOp(username) && Settings.OP_OVERRIDE) {
            return true;
        }
        for (int i = 0; i < this.getSizeInventory(); ++i) {
            final ItemStack itemStack = this.getStackInSlot(i);
            if (itemStack != null && itemStack.getItem() instanceof ICardIdentification
                && username.equalsIgnoreCase(
                    ((ICardIdentification) itemStack.getItem()).getUsername(itemStack)
                )
                && ((ICardIdentification) itemStack.getItem())
                       .hasPermission(itemStack, permission)) {
                return true;
            }
        }
        return username.equalsIgnoreCase(this.getOwner());
    }

    @Override
    public void onReceivePacket(PacketTile.Type type, final NBTTagCompound nbt) {
        super.onReceivePacket(type, nbt);
        if (type == PacketTile.Type.TOGGLE_MODE) {
            if (this.getManipulatingCard() != null) {
                final ICardIdentification idCard
                    = (ICardIdentification) this.getManipulatingCard().getItem();
                final int id = nbt.getInteger("buttonId");
                final Permission permission = Permission.getPermission(id);
                if (permission != null) {
                    if (!idCard.hasPermission(this.getManipulatingCard(), permission)) {
                        idCard.addPermission(this.getManipulatingCard(), permission);
                    } else {
                        idCard.removePermission(this.getManipulatingCard(), permission);
                    }
                } else {
                    ModularForceFieldSystem.LOGGER.severe(
                        "Error handling security station permission packet: " + id + " - "
                        + permission
                    );
                }
            }
        } else if (type == PacketTile.Type.STRING && this.getManipulatingCard() != null) {
            final ICardIdentification idCard
                = (ICardIdentification) this.getManipulatingCard().getItem();
            idCard.setUsername(this.getManipulatingCard(), nbt.getString("username"));
        }
    }

    @Override
    public boolean isItemValidForSlot(final int slotID, final ItemStack itemStack) {
        if (slotID == 0) {
            return itemStack.getItem() instanceof ItemCardFrequency;
        }
        return itemStack.getItem() instanceof ICardIdentification;
    }

    @Override
    public String getOwner() {
        final ItemStack itemStack = this.getStackInSlot(2);
        if (itemStack != null && itemStack.getItem() instanceof ICardIdentification) {
            return ((ICardIdentification) itemStack.getItem()).getUsername(itemStack);
        }
        return null;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (this.getManipulatingCard() != null && this.getStackInSlot(12) != null
            && this.getStackInSlot(12).getItem() instanceof ICardIdentification) {
            final ICardIdentification masterCard
                = (ICardIdentification) this.getManipulatingCard().getItem();
            final ICardIdentification copyCard
                = (ICardIdentification) this.getStackInSlot(12).getItem();
            for (final Permission permission : Permission.getPermissions()) {
                if (masterCard.hasPermission(this.getManipulatingCard(), permission)) {
                    copyCard.addPermission(this.getStackInSlot(12), permission);
                } else {
                    copyCard.removePermission(this.getStackInSlot(12), permission);
                }
            }
        }
    }

    @Override
    public int getSizeInventory() {
        return 13;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public ItemStack getManipulatingCard() {
        if (this.getStackInSlot(1) != null
            && this.getStackInSlot(1).getItem() instanceof ICardIdentification) {
            return this.getStackInSlot(1);
        }
        return null;
    }

    @Override
    public void setActive(final boolean flag) {
        if (this.getOwner() != null || !flag) {
            super.setActive(flag);
        }
    }

    @Override
    public Set<IBiometricIdentifier> getBiometricIdentifiers() {
        final Set<IBiometricIdentifier> set = new HashSet<>();
        set.add(this);
        return set;
    }
}
