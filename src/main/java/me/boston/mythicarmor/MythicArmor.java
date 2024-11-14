package me.boston.mythicarmor;

import com.mojang.logging.LogUtils;
import me.boston.mythicarmor.block.ModBlocks;
import me.boston.mythicarmor.block.entity.ModBlockEntities;
import me.boston.mythicarmor.component.ModDataComponentTypes;
import me.boston.mythicarmor.gui.ImbuingStationScreen;
import me.boston.mythicarmor.gui.ModMenuTypes;
import me.boston.mythicarmor.item.ModCreativeModeTabs;
import me.boston.mythicarmor.item.ModItems;
import me.boston.mythicarmor.mythic.ImbuementEffectsHandler;
import me.boston.mythicarmor.mythic.MythicItemHandler;
import me.boston.mythicarmor.mythic.MythicLavaCauldronInteraction;
import me.boston.mythicarmor.mythic.items.MythicElytraItem;
import me.boston.mythicarmor.rendering.MythicElytraArmorStandLayer;
import me.boston.mythicarmor.rendering.MythicElytraLayer;
import me.boston.mythicarmor.util.ModStats;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MythicArmor.MODID)
public class MythicArmor {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "mythicarmor";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "mythicarmor" namespace

    public MythicArmor(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        ModDataComponentTypes.register(modEventBus);
        ModMenuTypes.register(modEventBus);

        if(FMLEnvironment.dist.isClient())
            modEventBus.addListener(this::registerElytraLayer);


        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        MythicLavaCauldronInteraction.init();

        // Register classes for events
        MinecraftForge.EVENT_BUS.register(MythicItemHandler.class);
        MinecraftForge.EVENT_BUS.register(ImbuementEffectsHandler.class);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            MenuScreens.register(ModMenuTypes.IMBUING_STATION_MENU.get(), ImbuingStationScreen::new);
            ModStats.init();

            // broken elytra property
            ItemProperties.register(ModItems.MYTHIC_ELYTRA.get(), ResourceLocation.fromNamespaceAndPath(MODID, "broken"), (stack, level, entity, num) -> MythicElytraItem.isUsable(stack) ? 0 : 1);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void registerElytraLayer(EntityRenderersEvent event) {
        if (event instanceof EntityRenderersEvent.AddLayers addLayersEvent) {
            EntityModelSet entityModels = addLayersEvent.getEntityModels();
            addLayersEvent.getSkins().forEach(s -> {
                LivingEntityRenderer<? extends Player, ? extends EntityModel<? extends Player>> livingEntityRenderer = addLayersEvent.getPlayerSkin(s);
                if (livingEntityRenderer instanceof PlayerRenderer playerRenderer) {
                    playerRenderer.addLayer(new MythicElytraLayer(playerRenderer, entityModels));
                }
            });
            LivingEntityRenderer<ArmorStand, ? extends EntityModel<ArmorStand>> livingEntityRenderer = addLayersEvent.getEntityRenderer(EntityType.ARMOR_STAND);
            if (livingEntityRenderer instanceof ArmorStandRenderer armorStandRenderer) {
                armorStandRenderer.addLayer(new MythicElytraArmorStandLayer(armorStandRenderer, entityModels));
            }
        }
    }
}
