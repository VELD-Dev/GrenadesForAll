package com.velddev.grenades_test;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    static final ForgeConfigSpec SPEC = BUILDER.build();
    private static final ForgeConfigSpec.BooleanValue GRENADE_LAUNCH_ASSIST = BUILDER
        .comment("Whether the grenade should 100% reach the point you aim at or if there should be a class grenade launching system.")
        .define("grenadeAimAssist", true);
    public static boolean grenadeAimeAssist;
    
    private static boolean validateItemName(final Object obj)
    {
        return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName));
    }
    
    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        grenadeAimeAssist = GRENADE_LAUNCH_ASSIST.get();
    }
}
