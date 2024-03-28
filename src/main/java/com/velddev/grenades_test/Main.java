package com.velddev.grenades_test;

import com.mojang.logging.LogUtils;
import com.velddev.grenades_test.Grenades.GrenadeItem;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagUniverse;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import javax.swing.text.html.parser.Entity;

@Mod(Main.MODID)
public class Main
{
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String MODID = "veldsgrenadestest";
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    
    public static final RegistryObject<Block> EXAMPLE_BLOCK = BLOCKS.register("example_block",
        () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE))
    );
    
    
    //  ITEMS  //
    
    public static final RegistryObject<Item> EXAMPLE_BLOCK_ITEM = ITEMS.register("example_block",
        () -> new BlockItem(EXAMPLE_BLOCK.get(), new Item.Properties())
    );
    
    public static final RegistryObject<Item> SMOKE_GRENADE_ITEM = ITEMS.register("smoke_grenade",
        () -> new GrenadeItem(
            new Item.Properties()
                .defaultDurability(60)
                .durability(60)
                .fireResistant()
                .rarity(Rarity.UNCOMMON)
                .setNoRepair()
                .stacksTo(6),
            new GrenadeItem.Properties()
                .detonationDamage(0f)
                .mass(0.539f)
                .detonationRadius(3.5f)
                .throwSpeed(15.451f)
                .detonationSound("")
        )
    );
    
    
    //  CREATIVE TABS  //
    
    public static final RegistryObject<CreativeModeTab> GRENADES_TAB = CREATIVE_MODE_TABS.register("grenades_tab",
        () -> CreativeModeTab.builder().withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> SMOKE_GRENADE_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) ->
                {
                    output.accept(SMOKE_GRENADE_ITEM.get());
                    output.accept(EXAMPLE_BLOCK_ITEM.get());
                }
            ).build()
    );
    
    public Main()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        
        MinecraftForge.EVENT_BUS.register(this);
        
        modEventBus.addListener(this::addCreative);
        
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
    
    private void commonSetup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("--- GrenadesTest common setup ---");
        LOGGER.info("-> Grenade Aiming Assist: {}", Config.grenadeAimeAssist);
    }
    
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
            event.accept(EXAMPLE_BLOCK_ITEM);
    }
    
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info("HELLO from server starting");
    }
    
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
