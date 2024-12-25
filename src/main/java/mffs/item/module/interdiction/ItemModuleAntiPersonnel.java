package mffs.item.module.interdiction;

import mffs.ModularForceFieldSystem;
import mffs.Settings;
import mffs.api.security.IInterdictionMatrix;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;

public class ItemModuleAntiPersonnel extends ItemModuleInterdictionMatrix {
    public ItemModuleAntiPersonnel() {
        super("moduleAntiPersonnel");
    }

    @Override
    public boolean onDefend(
        final IInterdictionMatrix interdictionMatrix, final EntityLivingBase entityLiving
    ) {
        final boolean hasPermission = false;
        if (!hasPermission && entityLiving instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer) entityLiving;
            if (!player.capabilities.isCreativeMode && !player.isEntityInvulnerable()) {
                for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
                    if (player.inventory.getStackInSlot(i) != null) {
                        interdictionMatrix.mergeIntoInventory(
                            player.inventory.getStackInSlot(i)
                        );
                        player.inventory.setInventorySlotContents(i, (ItemStack) null);
                    }
                }
                player.attackEntityFrom(
                    ModularForceFieldSystem.damagefieldShock, Integer.MAX_VALUE
                );
                interdictionMatrix.requestFortron(
                    Settings.INTERDICTION_MURDER_ENERGY, false
                );
                player.addChatMessage(new ChatComponentText(
                    "[" + interdictionMatrix.getInventoryName() + "] Fairwell."
                ));
            }
        }
        return false;
    }
}
