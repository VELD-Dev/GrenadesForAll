package com.velddev.grenades_test.Utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.joml.Vector3d;
import org.joml.Vector3f;

import javax.annotation.Nullable;

public class Physics
{
    /**
     * @param time          time T in seconds
     * @param velocity      speed of the projectile in m/s
     * @param origin        position at which it was launched
     * @param gravity       gravity constraints for the projectile.
     * @param angleRadAlpha angle in radians (horizontal to vertical angle)
     * @param angleRadBeta  angle in radians (horizontal to depth angle)
     **/
    public static Vector3d throwPositionAt(float time, float velocity, Vector3f origin, float gravity,
                                           double angleRadAlpha, double angleRadBeta)
    {
        var x = Math.cos(angleRadAlpha) * velocity * time;
        var y = -(1f / 2f) * gravity * (time * time) + Math.sin(angleRadAlpha) * velocity * time;
        var z = Math.cos(angleRadBeta) * velocity * time;
        return new Vector3d(x, y, z).add(origin);
    }
    
    public static double angleForTrajectory(double heightInitial, double heightFinal, double distance, float velocity,
                                           float gravity)
    {
        double A = -(gravity * (distance * distance)) / (2 * (velocity * velocity));
        double C = (heightInitial - heightFinal + A);
        double delta = (distance * distance) - 4 * A * C;
        
        if (delta < 0)
        {
            return (Math.PI / 3);
        }
        else if (delta == 0)
        {
            return Math.atan((-distance) / (2 * A));
        }
        else
        {
            return Math.atan(((-distance) - Math.sqrt(delta)) / (2 * A));
        }
    }
    
    @Nullable
    public static ImpactPoint getImpactPointFromCurve(Level level, float velocity, Vector3f origin, float gravity,
                                             double AngleRadAlpha,
                                             double AngleRadBeta)
    {
        var hasImpacted = false;
        var time = 0f;
        ImpactPoint lastHit = null;
        while (!hasImpacted)
        {
            Vector3d root = throwPositionAt(time, velocity, origin, gravity, AngleRadAlpha, AngleRadBeta);
            Vector3d lim = throwPositionAt(time + 0.05f, velocity, origin, gravity, AngleRadAlpha, AngleRadBeta);
            var ctx = new ClipContext(
                new Vec3(root.x, root.y, root.z),
                new Vec3(lim.x, lim.y, lim.z),
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.ANY,
                null
            );
            HitResult hit = level.clip(ctx);
            
            switch (hit.getType())
            {
                case BLOCK:
                    lastHit = new ImpactPoint(time, (BlockHitResult) hit);
                    break;
                case ENTITY:
                    hit = HitResultsExt.getEntityHitResult(
                        level,
                        VectorUtils.toVec3(root),
                        VectorUtils.toVec3(lim),
                        new AABB(-0.1, -0.1, -0.1, 0.1, 0.1, 0.1),
                        (entity) -> entity.getType() != EntityType.ITEM && !entity.isSpectator(),
                        0.1
                    );
                    lastHit = new ImpactPoint(time, (EntityHitResult) hit);
                    break;
                default:
                    time += 0.05f;
                    if(time > 20f)
                        return null;
                    continue;
            }
            
            hasImpacted = true;
        }
        return lastHit;
    }
    
    public static class ImpactPoint
    {
        public final float time;
        public final BlockPos blockPosition;
        public final Vec3 precisePosition;
        public final Direction impactDirection;
        
        public ImpactPoint(float t, BlockHitResult hitResult)
        {
            time = t;
            impactDirection = hitResult.getDirection();
            blockPosition = hitResult.getBlockPos();
            precisePosition = hitResult.getLocation();
        }
        
        public ImpactPoint(float t, EntityHitResult hitResult)
        {
            time = t;
            impactDirection = Direction.DOWN;
            blockPosition = new BlockPos(
                (int) hitResult.getLocation().x,
                (int) hitResult.getLocation().y,
                (int) hitResult.getLocation().z
            );
            precisePosition = hitResult.getLocation();
        }
        
        public ImpactPoint(float t, HitResult hitResult)
        {
            time = t;
            impactDirection = Direction.DOWN;
            blockPosition = new BlockPos(
                (int) hitResult.getLocation().x,
                (int) hitResult.getLocation().y,
                (int) hitResult.getLocation().z
            );
            precisePosition = hitResult.getLocation();
        }
    }
}
