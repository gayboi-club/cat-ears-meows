package club.gayboi.catears.client;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import org.lwjgl.glfw.GLFW;

import club.gayboi.catears.CatEarsMod;
import club.gayboi.catears.CatEarsConfig;
import club.gayboi.catears.network.MeowConfigPayload;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import club.gayboi.catears.client.model.CatEarsModel;
import club.gayboi.catears.client.renderer.CatEarsLayer;

@EventBusSubscriber(modid = CatEarsMod.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {
    public static final KeyMapping CONFIG_KEY = new KeyMapping(
            "key.catears.config",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_K,
            "key.categories.catears"
    );

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        while (CONFIG_KEY.consumeClick()) {
            Minecraft.getInstance().setScreen(new CatEarsConfigScreen(null));
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(ClientPlayerNetworkEvent.LoggingIn event) {
        // sync meow preference on login :3
        try {
            PacketDistributor.sendToServer(new MeowConfigPayload(CatEarsConfig.ENABLE_MEOWING.get()));
        } catch (Exception e) {
            CatEarsMod.LOGGER.debug("Could not send meow config on login", e);
        }
    }

    @EventBusSubscriber(modid = CatEarsMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        public static final ModelLayerLocation CAT_EARS_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("catears", "cat_ears"), "main");

        @SubscribeEvent
        public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(CAT_EARS_LAYER, CatEarsModel::createBodyLayer);
        }

        @SubscribeEvent
        public static void addLayers(EntityRenderersEvent.AddLayers event) {
            CatEarsModel<LivingEntity> model = new CatEarsModel<>(event.getEntityModels().bakeLayer(CAT_EARS_LAYER));

            for (net.minecraft.client.resources.PlayerSkin.Model skin : event.getSkins()) {
                var renderer = event.getSkin(skin);
                if (renderer instanceof net.minecraft.client.renderer.entity.LivingEntityRenderer<?, ?> livingRenderer) {
                    addLayerToRenderer(livingRenderer, model);
                }
            }
        }

        @SuppressWarnings("unchecked")
        private static <T extends LivingEntity, M extends net.minecraft.client.model.EntityModel<T>> void addLayerToRenderer(net.minecraft.client.renderer.entity.LivingEntityRenderer<T, M> renderer, CatEarsModel<?> model) {
            renderer.addLayer(new CatEarsLayer<>(renderer, (CatEarsModel<T>) model));
        }
    }
}
