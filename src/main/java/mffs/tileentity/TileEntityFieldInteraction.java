package mffs.tileentity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import calclavia.lib.CalculationHelper;
import mffs.DelayedEvent;
import mffs.IDelayedEventHandler;
import mffs.ModularForceFieldSystem;
import mffs.Settings;
import mffs.api.ICache;
import mffs.api.IFieldInteraction;
import mffs.api.modules.IModule;
import mffs.api.modules.IProjectorMode;
import mffs.base.TileEntityModuleAcceptor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;

public abstract class TileEntityFieldInteraction
    extends TileEntityModuleAcceptor implements IFieldInteraction, IDelayedEventHandler {
    protected static final int MODULE_SLOT_ID = 2;
    protected boolean isCalculating;
    protected boolean isCalculated;
    protected final Set<Vector3> calculatedField;
    private final List<DelayedEvent> delayedEvents;
    private final List<DelayedEvent> quedDelayedEvents;

    public TileEntityFieldInteraction() {
        this.isCalculating = false;
        this.isCalculated = false;
        this.calculatedField = Collections.synchronizedSet(new HashSet<>());
        this.delayedEvents = new ArrayList<>();
        this.quedDelayedEvents = new ArrayList<>();
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (this.delayedEvents.size() > 0) {
            do {
                this.quedDelayedEvents.clear();
                final Iterator<DelayedEvent> it = this.delayedEvents.iterator();
                while (it.hasNext()) {
                    final DelayedEvent evt = it.next();
                    evt.update();
                    if (evt.ticks <= 0) {
                        it.remove();
                    }
                }
                this.delayedEvents.addAll(this.quedDelayedEvents);
            } while (!this.quedDelayedEvents.isEmpty());
        }
    }

    protected void
    calculateForceField(final ProjectorCalculationThread.IThreadCallBack callBack) {
        if (!this.getWorldObj().isRemote && !this.isCalculating
            && this.getMode() != null) {
            if (this.getModeStack().getItem() instanceof ICache) {
                ((ICache) this.getModeStack().getItem()).clearCache();
            }
            this.calculatedField.clear();
            new ProjectorCalculationThread(this, callBack).start();
        }
    }

    protected void calculateForceField() {
        this.calculateForceField(null);
    }

    @Override
    public ItemStack getModeStack() {
        if (this.getStackInSlot(2) != null
            && this.getStackInSlot(2).getItem() instanceof IProjectorMode) {
            return this.getStackInSlot(2);
        }
        return null;
    }

    @Override
    public IProjectorMode getMode() {
        if (this.getModeStack() != null) {
            return (IProjectorMode) this.getModeStack().getItem();
        }
        return null;
    }

    @Override
    public int
    getSidedModuleCount(final IModule module, final ForgeDirection... direction) {
        int count = 0;
        if (direction != null && direction.length > 0) {
            for (final ForgeDirection checkDir : direction) {
                count += this.getModuleCount(
                    module, this.getSlotsBasedOnDirection(checkDir)
                );
            }
        } else {
            for (int i = 0; i < 6; ++i) {
                final ForgeDirection checkDir2 = ForgeDirection.getOrientation(i);
                count += this.getModuleCount(
                    module, this.getSlotsBasedOnDirection(checkDir2)
                );
            }
        }
        return count;
    }

    @Override
    public int[] getModuleSlots() {
        return new int[] { 15, 16, 17, 18, 19, 20 };
    }

    @Override
    public Vector3 getTranslation() {
        final String cacheID = "getTranslation";
        if (Settings.USE_CACHE && super.cache.containsKey(cacheID)
            && super.cache.get(cacheID) instanceof Vector3) {
            return (Vector3) super.cache.get(cacheID);
        }
        ForgeDirection direction = this.getDirection(
            (IBlockAccess) this.getWorldObj(), this.xCoord, this.yCoord, this.zCoord
        );
        if (direction == ForgeDirection.UP || direction == ForgeDirection.DOWN) {
            direction = ForgeDirection.NORTH;
        }
        final int zTranslationNeg = this.getModuleCount(
            ModularForceFieldSystem.itemModuleTranslate,
            this.getSlotsBasedOnDirection(
                VectorHelper.getOrientationFromSide(direction, ForgeDirection.NORTH)
            )
        );
        final int zTranslationPos = this.getModuleCount(
            ModularForceFieldSystem.itemModuleTranslate,
            this.getSlotsBasedOnDirection(
                VectorHelper.getOrientationFromSide(direction, ForgeDirection.SOUTH)
            )
        );
        final int xTranslationNeg = this.getModuleCount(
            ModularForceFieldSystem.itemModuleTranslate,
            this.getSlotsBasedOnDirection(
                VectorHelper.getOrientationFromSide(direction, ForgeDirection.WEST)
            )
        );
        final int xTranslationPos = this.getModuleCount(
            ModularForceFieldSystem.itemModuleTranslate,
            this.getSlotsBasedOnDirection(
                VectorHelper.getOrientationFromSide(direction, ForgeDirection.EAST)
            )
        );
        final int yTranslationPos = this.getModuleCount(
            ModularForceFieldSystem.itemModuleTranslate,
            this.getSlotsBasedOnDirection(ForgeDirection.UP)
        );
        final int yTranslationNeg = this.getModuleCount(
            ModularForceFieldSystem.itemModuleTranslate,
            this.getSlotsBasedOnDirection(ForgeDirection.DOWN)
        );
        final Vector3 translation = new Vector3(
            xTranslationPos - xTranslationNeg,
            yTranslationPos - yTranslationNeg,
            zTranslationPos - zTranslationNeg
        );
        if (Settings.USE_CACHE) {
            super.cache.put(cacheID, translation);
        }
        return translation;
    }

    @Override
    public Vector3 getPositiveScale() {
        final String cacheID = "getPositiveScale";
        if (Settings.USE_CACHE && super.cache.containsKey(cacheID)
            && super.cache.get(cacheID) instanceof Vector3) {
            return (Vector3) super.cache.get(cacheID);
        }
        ForgeDirection direction = this.getDirection(
            (IBlockAccess) this.getWorldObj(), this.xCoord, this.yCoord, this.zCoord
        );
        if (direction == ForgeDirection.UP || direction == ForgeDirection.DOWN) {
            direction = ForgeDirection.NORTH;
        }
        int zScalePos = this.getModuleCount(
            ModularForceFieldSystem.itemModuleScale,
            this.getSlotsBasedOnDirection(
                VectorHelper.getOrientationFromSide(direction, ForgeDirection.SOUTH)
            )
        );
        int xScalePos = this.getModuleCount(
            ModularForceFieldSystem.itemModuleScale,
            this.getSlotsBasedOnDirection(
                VectorHelper.getOrientationFromSide(direction, ForgeDirection.EAST)
            )
        );
        int yScalePos = this.getModuleCount(
            ModularForceFieldSystem.itemModuleScale,
            this.getSlotsBasedOnDirection(ForgeDirection.UP)
        );
        final int omnidirectionalScale = this.getModuleCount(
            ModularForceFieldSystem.itemModuleScale, this.getModuleSlots()
        );
        zScalePos += omnidirectionalScale;
        xScalePos += omnidirectionalScale;
        yScalePos += omnidirectionalScale;
        final Vector3 positiveScale = new Vector3(xScalePos, yScalePos, zScalePos);
        if (Settings.USE_CACHE) {
            super.cache.put(cacheID, positiveScale);
        }
        return positiveScale;
    }

    @Override
    public Vector3 getNegativeScale() {
        final String cacheID = "getNegativeScale";
        if (Settings.USE_CACHE && super.cache.containsKey(cacheID)
            && super.cache.get(cacheID) instanceof Vector3) {
            return (Vector3) super.cache.get(cacheID);
        }
        ForgeDirection direction = this.getDirection(
            (IBlockAccess) this.getWorldObj(), this.xCoord, this.yCoord, this.zCoord
        );
        if (direction == ForgeDirection.UP || direction == ForgeDirection.DOWN) {
            direction = ForgeDirection.NORTH;
        }
        int zScaleNeg = this.getModuleCount(
            ModularForceFieldSystem.itemModuleScale,
            this.getSlotsBasedOnDirection(
                VectorHelper.getOrientationFromSide(direction, ForgeDirection.NORTH)
            )
        );
        int xScaleNeg = this.getModuleCount(
            ModularForceFieldSystem.itemModuleScale,
            this.getSlotsBasedOnDirection(
                VectorHelper.getOrientationFromSide(direction, ForgeDirection.WEST)
            )
        );
        int yScaleNeg = this.getModuleCount(
            ModularForceFieldSystem.itemModuleScale,
            this.getSlotsBasedOnDirection(ForgeDirection.DOWN)
        );
        final int omnidirectionalScale = this.getModuleCount(
            ModularForceFieldSystem.itemModuleScale, this.getModuleSlots()
        );
        zScaleNeg += omnidirectionalScale;
        xScaleNeg += omnidirectionalScale;
        yScaleNeg += omnidirectionalScale;
        final Vector3 negativeScale = new Vector3(xScaleNeg, yScaleNeg, zScaleNeg);
        if (Settings.USE_CACHE) {
            super.cache.put(cacheID, negativeScale);
        }
        return negativeScale;
    }

    @Override
    public int getRotationYaw() {
        final String cacheID = "getRotationYaw";
        if (Settings.USE_CACHE && super.cache.containsKey(cacheID)
            && super.cache.get(cacheID) instanceof Integer) {
            return (int) super.cache.get(cacheID);
        }
        final ForgeDirection direction = this.getDirection(
            (IBlockAccess) this.getWorldObj(), this.xCoord, this.yCoord, this.zCoord
        );
        int horizontalRotation
            = this.getModuleCount(
                  ModularForceFieldSystem.itemModuleRotate,
                  this.getSlotsBasedOnDirection(
                      VectorHelper.getOrientationFromSide(direction, ForgeDirection.EAST)
                  )
              )
            - this.getModuleCount(
                ModularForceFieldSystem.itemModuleRotate,
                this.getSlotsBasedOnDirection(
                    VectorHelper.getOrientationFromSide(direction, ForgeDirection.WEST)
                )
            )
            + this.getModuleCount(
                ModularForceFieldSystem.itemModuleRotate,
                this.getSlotsBasedOnDirection(
                    VectorHelper.getOrientationFromSide(direction, ForgeDirection.SOUTH)
                )
            )
            - this.getModuleCount(
                ModularForceFieldSystem.itemModuleRotate,
                this.getSlotsBasedOnDirection(
                    VectorHelper.getOrientationFromSide(direction, ForgeDirection.NORTH)
                )
            );
        horizontalRotation *= 2;
        if (Settings.USE_CACHE) {
            super.cache.put(cacheID, horizontalRotation);
        }
        return horizontalRotation;
    }

    @Override
    public int getRotationPitch() {
        final String cacheID = "getRotationPitch";
        if (Settings.USE_CACHE && super.cache.containsKey(cacheID)
            && super.cache.get(cacheID) instanceof Integer) {
            return (int) super.cache.get(cacheID);
        }
        int verticleRotation = this.getModuleCount(
                                   ModularForceFieldSystem.itemModuleRotate,
                                   this.getSlotsBasedOnDirection(ForgeDirection.UP)
                               )
            - this.getModuleCount(
                ModularForceFieldSystem.itemModuleRotate,
                this.getSlotsBasedOnDirection(ForgeDirection.DOWN)
            );
        verticleRotation *= 2;
        if (Settings.USE_CACHE) {
            super.cache.put(cacheID, verticleRotation);
        }
        return verticleRotation;
    }

    @Override
    public Set<Vector3> getInteriorPoints() {
        if (Settings.USE_CACHE && super.cache.containsKey("getInteriorPoints")
            && super.cache.get("getInteriorPoints") instanceof Set) {
            return (Set<Vector3>) super.cache.get("getInteriorPoints");
        }
        if (this.getModeStack().getItem() instanceof ICache) {
            ((ICache) this.getModeStack().getItem()).clearCache();
        }
        final Set<Vector3> newField = this.getMode().getInteriorPoints(this);
        final Set<Vector3> returnField = new HashSet<>();
        final Vector3 translation = this.getTranslation();
        final int rotationYaw = this.getRotationYaw();
        final int rotationPitch = this.getRotationPitch();
        for (final Vector3 position : newField) {
            final Vector3 newPosition = position.clone();
            if (rotationYaw != 0 || rotationPitch != 0) {
                CalculationHelper.rotateByAngle(newPosition, rotationYaw, rotationPitch);
            }
            newPosition.add(new Vector3(this));
            newPosition.add(translation);
            returnField.add(newPosition);
        }
        if (Settings.USE_CACHE) {
            super.cache.put("getInteriorPoints", returnField);
        }
        return returnField;
    }

    @Override
    public int[] getSlotsBasedOnDirection(final ForgeDirection direction) {
        switch (direction) {
            default: {
                return new int[0];
            }
            case UP: {
                return new int[] { 3, 11 };
            }
            case DOWN: {
                return new int[] { 6, 14 };
            }
            case NORTH: {
                return new int[] { 8, 10 };
            }
            case SOUTH: {
                return new int[] { 7, 9 };
            }
            case WEST: {
                return new int[] { 4, 5 };
            }
            case EAST: {
                return new int[] { 12, 13 };
            }
        }
    }

    @Override
    public void setCalculating(final boolean bool) {
        this.isCalculating = bool;
    }

    @Override
    public void setCalculated(final boolean bool) {
        this.isCalculated = bool;
    }

    @Override
    public Set<Vector3> getCalculatedField() {
        return this.calculatedField;
    }

    @Override
    public List<DelayedEvent> getDelayedEvents() {
        return this.delayedEvents;
    }

    @Override
    public List<DelayedEvent> getQuedDelayedEvents() {
        return this.quedDelayedEvents;
    }
}
