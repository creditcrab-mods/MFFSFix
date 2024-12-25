package mffs.base;

import java.util.List;

import mffs.MFFSCreativeTab;
import mffs.MFFSHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import universalelectricity.prefab.TranslationHelper;

public class ItemBase extends Item {
    public ItemBase(final String name) {
        super();
        this.setUnlocalizedName("mffs:" + name);
        this.setCreativeTab(MFFSCreativeTab.INSTANCE);
        this.setNoRepair();
        this.iconString = "mffs:" + name;
    }

    @Override
    public void addInformation(
        final ItemStack itemStack,
        final EntityPlayer player,
        final List info,
        final boolean b
    ) {
        final String tooltip
            = TranslationHelper.getLocal(this.getUnlocalizedName() + ".tooltip");
        if (tooltip != null && !tooltip.isEmpty()) {
            info.addAll(MFFSHelper.splitStringPerWord(tooltip, 5));
        }
    }
}
