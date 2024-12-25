package mffs.item.module;

import java.util.List;
import java.util.Set;

import mffs.api.IFieldInteraction;
import mffs.api.IProjector;
import mffs.api.modules.IModule;
import mffs.base.ItemBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;

public class ItemModule extends ItemBase implements IModule {
    private float fortronCost;

    public ItemModule(final String name) {
        super(name);
        this.fortronCost = 0.5f;
    }

    @Override
    public void addInformation(
        final ItemStack itemStack,
        final EntityPlayer player,
        final List info,
        final boolean b
    ) {
        info.add("Fortron: " + this.getFortronCost(1.0f));
        super.addInformation(itemStack, player, info, b);
    }

    @Override
    public void
    onCalculate(final IFieldInteraction projector, final Set<Vector3> position) {}

    @Override
    public boolean onProject(final IProjector projector, final Set<Vector3> fields) {
        return false;
    }

    @Override
    public int onProject(final IProjector projector, final Vector3 position) {
        return 0;
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
        return false;
    }

    public ItemModule setCost(final float cost) {
        this.fortronCost = cost;
        return this;
    }

    public ItemModule setMaxStackSize(final int par1) {
        super.setMaxStackSize(par1);
        return this;
    }

    @Override
    public float getFortronCost(final float amplifier) {
        return this.fortronCost * amplifier;
    }
}
