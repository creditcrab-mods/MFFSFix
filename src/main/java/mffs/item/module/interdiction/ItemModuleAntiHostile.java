package mffs.item.module.interdiction;

import mffs.ModularForceFieldSystem;
import mffs.api.security.IInterdictionMatrix;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.INpc;
import net.minecraft.entity.monster.IMob;

public class ItemModuleAntiHostile extends ItemModuleInterdictionMatrix {
    public ItemModuleAntiHostile() {
        super("moduleAntiHostile");
    }

    @Override
    public boolean onDefend(
        final IInterdictionMatrix interdictionMatrix, final EntityLivingBase entityLiving
    ) {
        if (entityLiving instanceof IMob && !(entityLiving instanceof INpc)) {
            entityLiving.attackEntityFrom(ModularForceFieldSystem.damagefieldShock, 20);
        }
        return false;
    }
}
