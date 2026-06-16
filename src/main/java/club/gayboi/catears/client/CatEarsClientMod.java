package club.gayboi.catears.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import club.gayboi.catears.CatEarsConfig;
import club.gayboi.catears.CatEarsMod;
import club.gayboi.catears.client.model.CatEarsModel;
import club.gayboi.catears.client.renderer.CatEarsLayer;
import club.gayboi.catears.network.MeowConfigPayload;

public class CatEarsClientMod implements ClientModInitializer {
    public static final ModelLayerLocation CAT_EARS_LAYER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(CatEarsMod.MOD_ID, "cat_ears"), "main");

    @Override
    public void onInitializeClient() {
        // register key mappings :3
        KeyBindingHelper.registerKeyBinding(ClientEvents.CONFIG_KEY);

        // register model layer :3
        EntityModelLayerRegistry.registerModelLayer(CAT_EARS_LAYER, CatEarsModel::createBodyLayer);

        // add layer to player renderers :3
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, renderer, helper, ctx) -> {
            if (entityType == EntityType.PLAYER) {
                var model = new CatEarsModel<>(
                        Minecraft.getInstance().getEntityModels().bakeLayer(CAT_EARS_LAYER));
                helper.register(new CatEarsLayer(renderer, model));
            }
        });

        // client tick for config key :3
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (ClientEvents.CONFIG_KEY.consumeClick()) {
                client.setScreen(new CatEarsConfigScreen(null));
            }
        });

        // sync meow preference on login :3
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            try {
                ClientPlayNetworking.send(new MeowConfigPayload(CatEarsConfig.enableMeowing));
            } catch (Exception e) {
                CatEarsMod.LOGGER.debug("Could not send meow config on login", e);
            }
        });
    }
}
