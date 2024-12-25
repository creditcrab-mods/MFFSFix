package mffs.item.card;

import java.util.List;

import mffs.MFFSHelper;
import mffs.api.card.ICardLink;
import mffs.card.ItemCard;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;

public class ItemCardLink extends ItemCard implements ICardLink {
    public ItemCardLink() {
        super("cardLink");
    }

    @Override
    public void addInformation(
        final ItemStack itemStack,
        final EntityPlayer player,
        final List list,
        final boolean b
    ) {
        final Vector3 position = this.getLink(itemStack);
        if (position != null) {
            final Block blockId
                = position.getBlock((IBlockAccess) ((Entity) player).worldObj);
            if (blockId != null) {
                list.add("Linked with: " + blockId.getLocalizedName());
                list.add(
                    position.intX() + ", " + position.intY() + ", " + position.intZ()
                );
                return;
            }
        }
        list.add("Not linked.");
    }

    public boolean onItemUse(
        final ItemStack itemStack,
        final EntityPlayer player,
        final World world,
        final int x,
        final int y,
        final int z,
        final int par7,
        final float par8,
        final float par9,
        final float par10
    ) {
        if (!world.isRemote) {
            final Vector3 vector = new Vector3(x, y, z);
            this.setLink(itemStack, vector);
            Block block = vector.getBlock(world);
            if (block != null) {
                player.addChatMessage(new ChatComponentText(
                    "Linked card to position: " + x + ", " + y + ", " + z
                    + " with block: " + block.getLocalizedName()
                ));
            }
        }
        return true;
    }

    @Override
    public void setLink(final ItemStack itemStack, final Vector3 position) {
        final NBTTagCompound nbt = MFFSHelper.getNBTTagCompound(itemStack);
        nbt.setTag("position", position.writeToNBT(new NBTTagCompound()));
    }

    @Override
    public Vector3 getLink(final ItemStack itemStack) {
        final NBTTagCompound nbt = MFFSHelper.getNBTTagCompound(itemStack);
        return Vector3.readFromNBT(nbt.getCompoundTag("position"));
    }
}
