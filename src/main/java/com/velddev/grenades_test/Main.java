package com.velddev.grenades_test;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
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

@Mod(Main.MODID)
public class Main
{
    public static final String MODID = "veldsgrenadestest";
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final RegistryObject<Block> EXAMPLE_BLOCK = BLOCKS.register("example_block",
        () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE))
    );
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final RegistryObject<Item> EXAMPLE_BLOCK_ITEM = ITEMS.register("example_block",
        () -> new BlockItem(EXAMPLE_BLOCK.get(), new Item.Properties())
    );
    
    public static final RegistryObject<Item> EXAMPLE_ITEM = ITEMS.register("example_item",
        () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder()
                .alwaysEat()
                .nutrition(1)
                .saturationMod(2f)
                .build()
            ))
    );
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    // Creates a creative tab with the id "examplemod:example_tab" for the example item, that is placed after the combat tab
    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab",
        () -> CreativeModeTab.builder().withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) ->
            {
                output.accept(EXAMPLE_ITEM.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
            }).build()
    );
    private static final Logger LOGGER = LogUtils.getLogger();
    
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
