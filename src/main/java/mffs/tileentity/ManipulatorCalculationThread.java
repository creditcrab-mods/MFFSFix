package mffs.tileentity;

import java.util.HashSet;
import java.util.Set;

import universalelectricity.core.vector.Vector3;

public class ManipulatorCalculationThread extends Thread {
    private TileEntityForceManipulator manipulator;
    private IThreadCallBack callBack;

    public ManipulatorCalculationThread(final TileEntityForceManipulator projector) {
        this.manipulator = projector;
    }

    public ManipulatorCalculationThread(
        final TileEntityForceManipulator projector, final IThreadCallBack callBack
    ) {
        this(projector);
        this.callBack = callBack;
    }

    @Override
    public void run() {
        this.manipulator.isCalculatingManipulation = true;
        try {
            final Set<Vector3> mobilizationPoints = this.manipulator.getInteriorPoints();
            if (this.manipulator.canMove()) {
                this.manipulator.manipulationVectors = new HashSet<>();
                for (final Vector3 position : mobilizationPoints) {
                    this.manipulator.manipulationVectors.add(position.clone());
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        this.manipulator.isCalculatingManipulation = false;
        if (this.callBack != null) {
            this.callBack.onThreadComplete();
        }
    }

    public interface IThreadCallBack {
        void onThreadComplete();
    }
}
