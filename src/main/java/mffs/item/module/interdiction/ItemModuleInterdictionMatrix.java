package mffs.item.module.interdiction;

import java.util.List;

import mffs.api.modules.IInterdictionMatrixModule;
import mffs.api.security.IInterdictionMatrix;
import mffs.item.module.ItemModule;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemModuleInterdictionMatrix
    extends ItemModule implements IInterdictionMatrixModule {
    public ItemModuleInterdictionMatrix(final String name) {
        super(name);
    }

    @Override
    public void addInformation(
        final ItemStack itemStack,
        final EntityPlayer player,
        final List info,
        final boolean b
    ) {
        info.add("§4Interdiction Matrix");
        super.addInformation(itemStack, player, info, b);
    }

    @Override
    public boolean onDefend(
        final IInterdictionMatrix interdictionMatrix, final EntityLivingBase entityLiving
    ) {
        return false;
    }
}
