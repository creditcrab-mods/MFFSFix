package mffs.item.card;

import java.util.List;

import icbm.api.IItemFrequency;
import mffs.base.TileEntityFrequency;
import mffs.card.ItemCard;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class ItemCardFrequency extends ItemCard implements IItemFrequency {
    public ItemCardFrequency(final String name) {
        super(name);
    }

    public ItemCardFrequency() {
        this("cardFrequency");
    }

    @Override
    public void addInformation(
        final ItemStack itemStack,
        final EntityPlayer par2EntityPlayer,
        final List list,
        final boolean par4
    ) {
        list.add("Frequency: " + this.getFrequency(itemStack));
    }

    @Override
    public int getFrequency(final ItemStack itemStack) {
        if (itemStack != null) {
            if (itemStack.getTagCompound() == null) {
                itemStack.setTagCompound(new NBTTagCompound());
            }
            return itemStack.getTagCompound().getInteger("frequency");
        }
        return 0;
    }

    @Override
    public void setFrequency(final int frequency, final ItemStack itemStack) {
        if (itemStack != null) {
            if (itemStack.getTagCompound() == null) {
                itemStack.setTagCompound(new NBTTagCompound());
            }
            itemStack.getTagCompound().setInteger("frequency", frequency);
        }
    }

    @Override
    public ItemStack onItemRightClick(
        final ItemStack itemStack, final World world, final EntityPlayer player
    ) {
        if (!world.isRemote && player.isSneaking()) {
            this.setFrequency(world.rand.nextInt(15), itemStack);
            player.addChatMessage(new ChatComponentText(
                "Generated random frequency: " + this.getFrequency(itemStack)
            ));
        }
        return itemStack;
    }

    @Override
    public boolean onItemUse(
        final ItemStack itemStack,
        final EntityPlayer player,
        final World world,
        final int x,
        final int y,
        final int z,
        final int side,
        final float hitX,
        final float hitY,
        final float hitZ
    ) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityFrequency) {
            if (!world.isRemote) {
                ((TileEntityFrequency) tileEntity)
                    .setFrequency(this.getFrequency(itemStack));
                world.markBlockForUpdate(x, y, z);
                player.addChatMessage(new ChatComponentText(
                    "Frequency set to: " + this.getFrequency(itemStack)
                ));
            }
            return true;
        }
        return false;
    }
}
