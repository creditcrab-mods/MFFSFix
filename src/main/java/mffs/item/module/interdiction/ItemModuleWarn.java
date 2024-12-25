package mffs.item.module.interdiction;

import mffs.api.security.IInterdictionMatrix;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

public class ItemModuleWarn extends ItemModuleInterdictionMatrix {
    public ItemModuleWarn() {
        super("moduleWarn");
    }

    @Override
    public boolean onDefend(
        final IInterdictionMatrix interdictionMatrix, final EntityLivingBase entityLiving
    ) {
        final boolean hasPermission = false;
        if (!hasPermission && entityLiving instanceof EntityPlayer) {
            ((EntityPlayer) entityLiving)
                .addChatMessage(new ChatComponentText(
                    "[" + interdictionMatrix.getInventoryName()
                    + "] Leave this zone immediately. You have no right to enter."
                ));
        }
        return false;
    }
}
