package mffs.item.card;

import java.util.List;

import mffs.MFFSHelper;
import mffs.api.card.ICardIdentification;
import mffs.api.security.Permission;
import mffs.card.ItemCard;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import universalelectricity.prefab.TranslationHelper;

public class ItemCardID extends ItemCard implements ICardIdentification {
    public ItemCardID() {
        super("cardIdentification");
    }

    public ItemCardID(final String name) {
        super(name);
    }

    @Override
    public boolean hitEntity(
        final ItemStack itemStack,
        final EntityLivingBase entityLiving,
        final EntityLivingBase par3EntityLiving
    ) {
        if (entityLiving instanceof EntityPlayer) {
            this.setUsername(itemStack, ((EntityPlayer) entityLiving).getDisplayName());
        }
        return false;
    }

    @Override
    public void addInformation(
        final ItemStack itemStack,
        final EntityPlayer player,
        final List info,
        final boolean b
    ) {
        if (this.getUsername(itemStack) != null
            && !this.getUsername(itemStack).isEmpty()) {
            info.add("Username: " + this.getUsername(itemStack));
        } else {
            info.add("Unidentified");
        }
        String tooltip = "";
        boolean isFirst = true;
        for (final Permission permission : Permission.getPermissions()) {
            if (this.hasPermission(itemStack, permission)) {
                if (!isFirst) {
                    tooltip += ", ";
                }
                isFirst = false;
                tooltip += TranslationHelper.getLocal("gui." + permission.name + ".name");
            }
        }
        if (tooltip != null && tooltip.length() > 0) {
            info.addAll(MFFSHelper.splitStringPerWord(tooltip, 5));
        }
    }

    @Override
    public ItemStack onItemRightClick(
        final ItemStack itemStack, final World par2World, final EntityPlayer entityPlayer
    ) {
        this.setUsername(itemStack, entityPlayer.getDisplayName());
        return itemStack;
    }

    @Override
    public void setUsername(final ItemStack itemStack, final String username) {
        final NBTTagCompound nbtTagCompound = MFFSHelper.getNBTTagCompound(itemStack);
        nbtTagCompound.setString("name", username);
    }

    @Override
    public String getUsername(final ItemStack itemStack) {
        final NBTTagCompound nbtTagCompound = MFFSHelper.getNBTTagCompound(itemStack);
        if (nbtTagCompound != null && nbtTagCompound.getString("name") != "") {
            return nbtTagCompound.getString("name");
        }
        return null;
    }

    @Override
    public boolean hasPermission(final ItemStack itemStack, final Permission permission) {
        final NBTTagCompound nbt = MFFSHelper.getNBTTagCompound(itemStack);
        return nbt.getBoolean("mffs_permission_" + permission.id);
    }

    @Override
    public boolean addPermission(final ItemStack itemStack, final Permission permission) {
        final NBTTagCompound nbt = MFFSHelper.getNBTTagCompound(itemStack);
        nbt.setBoolean("mffs_permission_" + permission.id, true);
        return false;
    }

    @Override
    public boolean
    removePermission(final ItemStack itemStack, final Permission permission) {
        final NBTTagCompound nbt = MFFSHelper.getNBTTagCompound(itemStack);
        nbt.setBoolean("mffs_permission_" + permission.id, false);
        return false;
    }
}
