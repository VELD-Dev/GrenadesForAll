package com.velddev.grenades_test.Grenades;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public abstract class GrenadeBehaviour
{
    public GrenadeBehaviour() {}
    
    public abstract void detonate(Vec3 position, float radius, float damage);
    
    public abstract void onUseTick(GrenadeItem item, Level level, LivingEntity user, ItemStack stack, int remainingUseDuration);
}
