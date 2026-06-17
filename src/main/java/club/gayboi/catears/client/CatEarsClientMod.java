package club.gayboi.catears.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityRenderLayerRegistrationCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;

import club.gayboi.catears.CatEarsConfig;
import club.gayboi.catears.CatEarsMod;
import club.gayboi.catears.client.model.CatEarsModel;
import club.gayboi.catears.client.renderer.CatEarsLayer;
import club.gayboi.catears.network.MeowConfigPayload;

public class CatEarsClientMod implements ClientModInitializer {
    public static final ModelLayerLocation CAT_EARS_LAYER = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(CatEarsMod.MOD_ID, "cat_ears"), "main");

    @Override
    public void onInitializeClient() {
        // register key mappings :3
        KeyMappingHelper.registerKeyMapping(ClientEvents.CONFIG_KEY);

        // register model layer :3
        ModelLayerRegistry.registerModelLayer(CAT_EARS_LAYER, CatEarsModel::createBodyLayer);

        // add layer to player renderers :3
        LivingEntityRenderLayerRegistrationCallback.EVENT.register((entityType, renderer, helper, context) -> {
            if (entityType == EntityType.PLAYER) {
                var model = new CatEarsModel(
                        Minecraft.getInstance().getEntityModels().bakeLayer(CAT_EARS_LAYER));
                @SuppressWarnings({"unchecked", "rawtypes"})
                var layer = new CatEarsLayer(renderer, model);
                @SuppressWarnings({"rawtypes"})
                var typedHelper = helper;
                typedHelper.register(layer);
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
