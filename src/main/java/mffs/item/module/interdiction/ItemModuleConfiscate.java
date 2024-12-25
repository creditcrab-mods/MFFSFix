package mffs.item.module.interdiction;

import java.util.Set;

import mffs.api.security.IBiometricIdentifier;
import mffs.api.security.IInterdictionMatrix;
import mffs.api.security.Permission;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;

public class ItemModuleConfiscate extends ItemModuleInterdictionMatrix {
    public ItemModuleConfiscate() {
        super("moduleConfiscate");
    }

    @Override
    public boolean onDefend(
        final IInterdictionMatrix interdictionMatrix, final EntityLivingBase entityLiving
    ) {
        if (entityLiving instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer) entityLiving;
            final IBiometricIdentifier biometricIdentifier
                = interdictionMatrix.getBiometricIdentifier();
            if (biometricIdentifier != null
                && biometricIdentifier.isAccessGranted(
                    player.getDisplayName(), Permission.DEFENSE_STATION_CONFISCATION
                )) {
                return false;
            }
        }
        final Set<ItemStack> controlledStacks = interdictionMatrix.getFilteredItems();
        int confiscationCount = 0;
        IInventory inventory = null;
        if (entityLiving instanceof EntityPlayer) {
            final IBiometricIdentifier biometricIdentifier2
                = interdictionMatrix.getBiometricIdentifier();
            if (biometricIdentifier2 != null
                && biometricIdentifier2.isAccessGranted(
                    ((EntityPlayer) entityLiving).getDisplayName(),
                    Permission.BYPASS_INTERDICTION_MATRIX
                )) {
                return false;
            }
            final EntityPlayer player2 = (EntityPlayer) entityLiving;
            inventory = (IInventory) player2.inventory;
        } else if (entityLiving instanceof IInventory) {
            inventory = (IInventory) entityLiving;
        }
        if (inventory != null) {
            for (int i = 0; i < inventory.getSizeInventory(); ++i) {
                final ItemStack checkStack = inventory.getStackInSlot(i);
                if (checkStack != null) {
                    boolean stacksMatch = false;
                    for (final ItemStack itemStack : controlledStacks) {
                        if (itemStack != null && itemStack.isItemEqual(checkStack)) {
                            stacksMatch = true;
                            break;
                        }
                    }
                    if ((interdictionMatrix.getFilterMode() && stacksMatch)
                        || (!interdictionMatrix.getFilterMode() && !stacksMatch)) {
                        interdictionMatrix.mergeIntoInventory(inventory.getStackInSlot(i)
                        );
                        inventory.setInventorySlotContents(i, (ItemStack) null);
                        ++confiscationCount;
                    }
                }
            }
            if (confiscationCount > 0 && entityLiving instanceof EntityPlayer) {
                ((EntityPlayer) entityLiving)
                    .addChatMessage(new ChatComponentText(
                        "[" + interdictionMatrix.getInventoryName() + "] "
                        + confiscationCount + " of your item(s) has been confiscated."
                    ));
            }
            interdictionMatrix.requestFortron(confiscationCount, true);
        }
        return false;
    }
}
