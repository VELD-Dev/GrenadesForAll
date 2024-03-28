package com.velddev.grenades_test.Grenades;

import com.velddev.grenades_test.Config;
import com.velddev.grenades_test.Utils.Physics;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public class GrenadeItem extends Item
{
    private final float detonationRadius;
    private final String detonationSoundId;
    private final float detonationDamage;
    private final float throwSpeed;
    private final float mass;
    private GrenadeBehaviour grenadeBehaviour;
    
    public GrenadeItem(Item.Properties prop, Properties greProp)
    {
        super(prop);
        detonationRadius = greProp.detonationRadius;
        detonationSoundId = greProp.detonationSoundId;
        detonationDamage = greProp.detonationDamage;
        throwSpeed = greProp.throwSpeed;
        mass = greProp.mass;
        if (greProp.behaviour != null)
        {
            grenadeBehaviour = greProp.behaviour;
        }
    }
        
        @Override
        public InteractionResultHolder<ItemStack> use (Level level, Player player, InteractionHand hand)
        {
            double gravity = Config.grenadesDimensionsGravity.get(0);
            if (Config.grenadeAimeAssist)
            {
                BlockHitResult bHitRes = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);
                Vec3 lookPos = bHitRes.getLocation();
                double distance = player.distanceToSqr(lookPos);
                double angleAlpha = Physics.angleForTrajectory(
                    player.position().y,
                    lookPos.y,
                    distance,
                    throwSpeed,
                    gravity
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
    
    @Override
    public void onUseTick(Level level, LivingEntity user, ItemStack stack, int remainingUseDuration)
    {
        super.onUseTick(level, user, stack, remainingUseDuration);
        if(grenadeBehaviour != null) grenadeBehaviour.onUseTick(this, level, user, stack, remainingUseDuration);
    }
    
    public static class Properties
        {
            float detonationRadius = 3f;
            String detonationSoundId = "";
            float detonationDamage = 0f;
            float throwSpeed = 4f;
            float mass = 0.5f;
            GrenadeBehaviour behaviour;
            Entity grenadeEntity;
            
            public Properties()
            {
            }
            
            public Properties detonationRadius(float radius)
            {
                detonationRadius = radius;
                return this;
            }
            
            public Properties detonationSound(String soundId)
            {
                detonationSoundId = soundId;
                return this;
            }
            
            public Properties detonationDamage(float damageAmount)
            {
                detonationDamage = damageAmount;
                return this;
            }
            
            public Properties throwSpeed(float velocity)
            {
                throwSpeed = velocity;
                return this;
            }
            
            public Properties mass(float massKg)
            {
                mass = massKg;
                return this;
            }
            
            public Properties withBehaviour(GrenadeBehaviour grenadeBehaviour)
            {
                behaviour = grenadeBehaviour;
                return this;
            }
        }
    }
