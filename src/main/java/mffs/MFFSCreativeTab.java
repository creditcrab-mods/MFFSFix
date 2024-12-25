package mffs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class MFFSCreativeTab extends CreativeTabs {
    public static CreativeTabs INSTANCE;

    public MFFSCreativeTab(final int par1, final String par2Str) {
        super(par1, par2Str);
    }

    @Override
    public Item getTabIconItem() {
        return Item.getItemFromBlock(ModularForceFieldSystem.blockForceFieldProjector);
    }

    static {
        MFFSCreativeTab.INSTANCE = new MFFSCreativeTab(CreativeTabs.getNextID(), "MFFS");
    }
}
