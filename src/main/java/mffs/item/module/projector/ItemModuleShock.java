package mffs.item.module.projector;

import mffs.ModularForceFieldSystem;
import mffs.item.module.ItemModule;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemModuleShock extends ItemModule {
    public ItemModuleShock() {
        super("moduleShock");
    }

    @Override
    public boolean onCollideWithForceField(
        final World world,
        final int x,
        final int y,
        final int z,
        final Entity entity,
        final ItemStack moduleStack
    ) {
        if (entity instanceof EntityLiving) {
            entity.attackEntityFrom(
                ModularForceFieldSystem.damagefieldShock, moduleStack.stackSize
            );
        }
        return false;
    }
}
