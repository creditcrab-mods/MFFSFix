package mffs.item.module.projector;

import java.util.Iterator;
import java.util.Set;

import icbm.api.IBlockFrequency;
import mffs.api.IFieldInteraction;
import mffs.api.IProjector;
import mffs.api.fortron.IFortronFrequency;
import mffs.base.TileEntityBase;
import mffs.fortron.FrequencyGrid;
import mffs.item.module.ItemModule;
import net.minecraft.tileentity.TileEntity;
import universalelectricity.core.vector.Vector3;

public class ItemModuleFusion extends ItemModule {
    public ItemModuleFusion() {
        super("moduleFusion");
        this.setMaxStackSize(1);
        this.setCost(1.0f);
    }

    @Override
    public boolean onProject(final IProjector projector, final Set<Vector3> fieldBlocks) {
        final Set<IBlockFrequency> machines
            = FrequencyGrid.instance().get(((IFortronFrequency) projector).getFrequency()
            );
        for (final IBlockFrequency compareProjector : machines) {
            if (compareProjector instanceof IProjector && compareProjector != projector
                && ((TileEntity) compareProjector).getWorldObj()
                    == ((TileEntity) projector).getWorldObj()
                && ((TileEntityBase) compareProjector).isActive()
                && ((IProjector) compareProjector).getMode() != null) {
                final Iterator<Vector3> it = fieldBlocks.iterator();
                while (it.hasNext()) {
                    final Vector3 position = it.next();
                    if (((IProjector) compareProjector)
                            .getMode()
                            .isInField(
                                (IFieldInteraction) compareProjector, position.clone()
                            )) {
                        it.remove();
                    }
                }
            }
        }
        return false;
    }
}
