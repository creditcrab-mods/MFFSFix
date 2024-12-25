package mffs.item.module.interdiction;

import mffs.ModularForceFieldSystem;
import mffs.api.security.IInterdictionMatrix;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.INpc;
import net.minecraft.entity.monster.IMob;

public class ItemModuleAntiFriendly extends ItemModuleInterdictionMatrix {
    public ItemModuleAntiFriendly() {
        super("moduleAntiFriendly");
    }

    @Override
    public boolean onDefend(
        final IInterdictionMatrix interdictionMatrix, final EntityLivingBase entityLiving
    ) {
        if (!(entityLiving instanceof IMob) || entityLiving instanceof INpc) {
            entityLiving.attackEntityFrom(
                ModularForceFieldSystem.damagefieldShock, Integer.MAX_VALUE
            );
        }
        return false;
    }
}
