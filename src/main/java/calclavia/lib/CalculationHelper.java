package calclavia.lib;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;

public class CalculationHelper {
    public static void rotateByAngle(Vector3 vector, double yaw) {
        double yawRadians = Math.toRadians(yaw);
        double x = vector.x;
        double z = vector.z;
        if (yaw != 0.0D) {
            vector.x = x * Math.cos(yawRadians) - z * Math.sin(yawRadians);
            vector.z = x * Math.sin(yawRadians) + z * Math.cos(yawRadians);
        }
    }

    public static void rotateByAngle(Vector3 vector, double yaw, double pitch) {
        rotateByAngle(vector, yaw, pitch, 0.0D);
    }

    public static void
    rotateByAngle(Vector3 vector, double yaw, double pitch, double roll) {
        double yawRadians = Math.toRadians(yaw);
        double pitchRadians = Math.toRadians(pitch);
        double rollRadians = Math.toRadians(roll);
        double x = vector.x;
        double y = vector.y;
        double z = vector.z;
        vector.x = x * Math.cos(yawRadians) * Math.cos(pitchRadians)
            + z
                * (Math.cos(yawRadians) * Math.sin(pitchRadians) * Math.sin(rollRadians)
                   - Math.sin(yawRadians) * Math.cos(rollRadians))
            + y
                * (Math.cos(yawRadians) * Math.sin(pitchRadians) * Math.cos(rollRadians)
                   + Math.sin(yawRadians) * Math.sin(rollRadians));
        vector.z = x * Math.sin(yawRadians) * Math.cos(pitchRadians)
            + z
                * (Math.sin(yawRadians) * Math.sin(pitchRadians) * Math.sin(rollRadians)
                   + Math.cos(yawRadians) * Math.cos(rollRadians))
            + y
                * (Math.sin(yawRadians) * Math.sin(pitchRadians) * Math.cos(rollRadians)
                   - Math.cos(yawRadians) * Math.sin(rollRadians));
        vector.y = -x * Math.sin(pitchRadians)
            + z * Math.cos(pitchRadians) * Math.sin(rollRadians)
            + y * Math.cos(pitchRadians) * Math.cos(rollRadians);
    }

    public static Vector3
    getDeltaPositionFromRotation(float rotationYaw, float rotationPitch) {
        rotationYaw += 90.0F;
        rotationPitch = -rotationPitch;
        return new Vector3(
            Math.cos(Math.toRadians((double) rotationYaw)),
            Math.sin(Math.toRadians((double) rotationPitch)),
            Math.sin(Math.toRadians((double) rotationYaw))
        );
    }

    public static MovingObjectPosition raytraceEntities(
        World world,
        Vector3 startPosition,
        float rotationYaw,
        float rotationPitch,
        boolean collisionFlag,
        double reachDistance
    ) {
        MovingObjectPosition pickedEntity = null;
        Vec3 startingPosition = startPosition.toVec3();
        Vec3 look = getDeltaPositionFromRotation(rotationYaw, rotationPitch).toVec3();
        Vec3 reachPoint = Vec3.createVectorHelper(
            startingPosition.xCoord + look.xCoord * reachDistance,
            startingPosition.yCoord + look.yCoord * reachDistance,
            startingPosition.zCoord + look.zCoord * reachDistance
        );
        double playerBorder = 1.1D * reachDistance;
        AxisAlignedBB boxToScan = AxisAlignedBB.getBoundingBox(
            -playerBorder,
            -playerBorder,
            -playerBorder,
            playerBorder,
            playerBorder,
            playerBorder
        );
        List entitiesHit
            = world.getEntitiesWithinAABBExcludingEntity((Entity) null, boxToScan);
        double closestEntity = reachDistance;
        if (entitiesHit != null && !entitiesHit.isEmpty()) {
            Iterator i$ = entitiesHit.iterator();

            while (i$.hasNext()) {
                Entity entityHit = (Entity) i$.next();
                if (entityHit != null && entityHit.canBeCollidedWith()
                    && entityHit.boundingBox != null) {
                    float border = entityHit.getCollisionBorderSize();
                    AxisAlignedBB aabb = entityHit.boundingBox.expand(
                        (double) border, (double) border, (double) border
                    );
                    MovingObjectPosition hitMOP
                        = aabb.calculateIntercept(startingPosition, reachPoint);
                    if (hitMOP != null) {
                        if (aabb.isVecInside(startingPosition)) {
                            if (0.0D < closestEntity || closestEntity == 0.0D) {
                                pickedEntity = new MovingObjectPosition(entityHit);
                                if (pickedEntity != null) {
                                    pickedEntity.hitVec = hitMOP.hitVec;
                                    closestEntity = 0.0D;
                                }
                            }
                        } else {
                            double distance = startingPosition.distanceTo(hitMOP.hitVec);
                            if (distance < closestEntity || closestEntity == 0.0D) {
                                pickedEntity = new MovingObjectPosition(entityHit);
                                pickedEntity.hitVec = hitMOP.hitVec;
                                closestEntity = distance;
                            }
                        }
                    }
                }
            }

            return pickedEntity;
        } else {
            return null;
        }
    }

    public static MovingObjectPosition raytraceBlocks(
        World world,
        Vector3 startPosition,
        float rotationYaw,
        float rotationPitch,
        boolean collisionFlag,
        double reachDistance
    ) {
        Vector3 lookVector = getDeltaPositionFromRotation(rotationYaw, rotationPitch);
        Vector3 reachPoint
            = Vector3.add(startPosition, Vector3.multiply(lookVector, reachDistance));
        return world.rayTraceBlocks(
            startPosition.toVec3(), reachPoint.toVec3(), collisionFlag
        );
    }

    public static MovingObjectPosition doCustomRayTrace(
        World world,
        Vector3 startPosition,
        float rotationYaw,
        float rotationPitch,
        boolean collisionFlag,
        double reachDistance
    ) {
        MovingObjectPosition pickedBlock = raytraceBlocks(
            world, startPosition, rotationYaw, rotationPitch, collisionFlag, reachDistance
        );
        MovingObjectPosition pickedEntity = raytraceEntities(
            world, startPosition, rotationYaw, rotationPitch, collisionFlag, reachDistance
        );
        if (pickedBlock == null) {
            return pickedEntity;
        } else if (pickedEntity == null) {
            return pickedBlock;
        } else {
            double dBlock = startPosition.distanceTo(new Vector3(pickedBlock.hitVec));
            double dEntity = startPosition.distanceTo(new Vector3(pickedEntity.hitVec));
            return dEntity < dBlock ? pickedEntity : pickedBlock;
        }
    }
}
