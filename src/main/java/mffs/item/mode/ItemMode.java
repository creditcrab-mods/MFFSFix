package mffs.item.mode;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mffs.api.IFieldInteraction;
import mffs.api.IProjector;
import mffs.api.modules.IProjectorMode;
import mffs.base.ItemBase;
import universalelectricity.core.vector.Vector3;

public abstract class ItemMode extends ItemBase implements IProjectorMode {
    public ItemMode(final String name) {
        super(name);
        this.setMaxStackSize(1);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void render(
        final IProjector projector,
        final double x,
        final double y,
        final double z,
        final float f,
        final long ticks
    ) {}

    @Override
    public boolean isInField(final IFieldInteraction projector, final Vector3 position) {
        return false;
    }
}
