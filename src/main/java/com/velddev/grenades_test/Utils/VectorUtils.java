package com.velddev.grenades_test.Utils;

import net.minecraft.world.phys.Vec3;
import org.checkerframework.common.returnsreceiver.qual.This;
import org.joml.Vector3d;

public class VectorUtils
{
    public static Vec3 toVec3(@This Vector3d vector)
    {
        return new Vec3(vector.x, vector.y, vector.z);
    }
}
