package com.velddev.grenades_test.Grenades;

import com.velddev.grenades_test.Config;
import com.velddev.grenades_test.Utils.Physics;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

public abstract class Grenade extends Item
{
    private final float detonationRadius;
    private final String detonationSoundId;
    private final float detonationDamage;
    private final float throwSpeed;
    private final float mass;
    
    public Grenade(Properties prop, GrenadeProperties grenadeProp)
    {
        super(prop);
        detonationRadius = grenadeProp.detonationRadius;
        detonationSoundId = grenadeProp.detonationSoundId;
        detonationDamage = grenadeProp.detonationDamage;
        ;
        throwSpeed = grenadeProp.throwSpeed;
        mass = grenadeProp.mass;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        if (Config.grenadeAimeAssist)
        {
            BlockHitResult bHitRes = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);
            Vec3 lookPos = bHitRes.getLocation();
            double distance = player.distanceToSqr(lookPos);
            double angleAlpha = Physics.angleForTrajectory(
                (float) player.position().y,
                (float) lookPos.y,
                (float) distance,
                throwSpeed,
                9.81f
            );
        }
        else
        {
            Quaternionf qt =
                getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE).getDirection().getRotation();
            double qsin = Math.sqrt(1 + 2 * (qt.w * qt.y - qt.x * qt.z));
            double qcos = Math.sqrt(1 - 2 * (qt.w * qt.y - qt.x * qt.z));
            double aimAlphaAngle = 2 * Math.atan2(qsin, qcos) - Math.PI / 2;
            
        }
        
        return super.use(level, player, hand);
    }
    
    public static class GrenadeProperties
    {
        float detonationRadius = 3f;
        String detonationSoundId = "";
        float detonationDamage = 0f;
        float throwSpeed = 4f;
        float mass = 0.5f;
        
        public GrenadeProperties()
        {
        }
        
        public GrenadeProperties detonationRadius(float radius)
        {
            detonationRadius = radius;
            return this;
        }
        
        public GrenadeProperties detonationSound(String soundId)
        {
            detonationSoundId = soundId;
            return this;
        }
        
        public GrenadeProperties detonationDamage(float damageAmount)
        {
            detonationDamage = damageAmount;
            return this;
        }
        
        public GrenadeProperties throwSpeed(float velocity)
        {
            throwSpeed = velocity;
            return this;
        }
        
        public GrenadeProperties mass(float massKg)
        {
            mass = massKg;
            return this;
        }
    }
}
