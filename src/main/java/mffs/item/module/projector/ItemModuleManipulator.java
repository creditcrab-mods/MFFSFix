package mffs.item.module.projector;

import java.util.Iterator;
import java.util.Set;

import mffs.api.IFieldInteraction;
import mffs.item.module.ItemModule;
import net.minecraft.tileentity.TileEntity;
import universalelectricity.core.vector.Vector3;

public class ItemModuleManipulator extends ItemModule {
    public ItemModuleManipulator() {
        super("moduleManipulator");
    }

    @Override
    public void
    onCalculate(final IFieldInteraction projector, final Set<Vector3> fieldBlocks) {
        final Iterator<Vector3> it = fieldBlocks.iterator();
        while (it.hasNext()) {
            final Vector3 position = it.next();
            if (position.y < ((TileEntity) projector).yCoord) {
                it.remove();
            }
        }
    }
}
