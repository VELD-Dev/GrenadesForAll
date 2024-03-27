package com.velddev.grenades_test.Utils;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Predicate;

public class HitResultsExt
{
    @Nullable
    public static EntityHitResult getEntityHitResult(Level level, Vec3 startVec, Vec3 endVec, AABB boundingBox, Predicate<Entity> filter, double distance)
    {
        double d0 = distance;
        Entity entity = null;
        Vec3 vec3 = null;
        
        for (Entity entity1 : level.getEntities((Entity) null, boundingBox, filter))
        {
            AABB aabb = entity1.getBoundingBox().inflate((double) entity1.getPickRadius());
            Optional<Vec3> optional = aabb.clip(startVec, endVec);
            if (aabb.contains(startVec))
            {
                if (d0 >= 0.0)
                {
                    entity = entity1;
                    vec3 = (Vec3) optional.orElse(startVec);
                    d0 = 0.0;
                }
            } else if (optional.isPresent())
            {
                Vec3 vec31 = (Vec3) optional.get();
                double d1 = startVec.distanceToSqr(vec31);
                if (d1 < d0 || d0 == 0.0)
                {
                    entity = entity1;
                    vec3 = vec31;
                    d0 = d1;
                }
            }
        }
        
        return entity == null ? null : new EntityHitResult(entity, vec3);
    }
}
