package mffs.tileentity;

import java.util.Set;

import calclavia.lib.CalculationHelper;
import mffs.ModularForceFieldSystem;
import mffs.api.IFieldInteraction;
import mffs.api.modules.IModule;
import net.minecraft.tileentity.TileEntity;
import universalelectricity.core.vector.Vector3;

public class ProjectorCalculationThread extends Thread {
    private IFieldInteraction projector;
    private IThreadCallBack callBack;

    public ProjectorCalculationThread(final IFieldInteraction projector) {
        this.projector = projector;
    }

    public ProjectorCalculationThread(
        final IFieldInteraction projector, final IThreadCallBack callBack
    ) {
        this(projector);
        this.callBack = callBack;
    }

    @Override
    public void run() {
        this.projector.setCalculating(true);
        try {
            if (this.projector.getMode() != null) {
                Set<Vector3> newField;
                if (this.projector.getModuleCount(
                        ModularForceFieldSystem.itemModuleInvert, new int[0]
                    )
                    > 0) {
                    newField = this.projector.getMode().getInteriorPoints(this.projector);
                } else {
                    newField = this.projector.getMode().getExteriorPoints(this.projector);
                }
                final Vector3 translation = this.projector.getTranslation();
                final int rotationYaw = this.projector.getRotationYaw();
                final int rotationPitch = this.projector.getRotationPitch();
                for (final Vector3 position : newField) {
                    if (rotationYaw != 0 || rotationPitch != 0) {
                        CalculationHelper.rotateByAngle(
                            position, rotationYaw, rotationPitch
                        );
                    }
                    position.add(new Vector3((TileEntity) this.projector));
                    position.add(translation);
                    if (position.intY()
                        <= ((TileEntity) this.projector).getWorldObj().getHeight()) {
                        this.projector.getCalculatedField().add(position.round());
                    }
                }
                for (final IModule module :
                     this.projector.getModules(this.projector.getModuleSlots())) {
                    module.onCalculate(
                        this.projector, this.projector.getCalculatedField()
                    );
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        this.projector.setCalculating(false);
        this.projector.setCalculated(true);
        if (this.callBack != null) {
            this.callBack.onThreadComplete();
        }
    }

    public interface IThreadCallBack {
        void onThreadComplete();
    }
}
