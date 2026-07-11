package club.gayboi.catears.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;

import club.gayboi.catears.CatEarsConfig;
import club.gayboi.catears.CatEarsMod;
import club.gayboi.catears.client.model.CatEarsModel;
import club.gayboi.catears.client.renderer.CatEarsLayer;
import club.gayboi.catears.network.MeowConfigPayload;
import club.gayboi.catears.network.SyncEarDataPayload;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class CatEarsClientMod implements ClientModInitializer {
    public static final ModelLayerLocation CAT_EARS_LAYER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(CatEarsMod.MOD_ID, "cat_ears"), "main");

    public static boolean serverHasMod = false;
    public static final Map<UUID, SyncEarDataPayload> remoteEarData = new ConcurrentHashMap<>();

    private static final Pattern PURR_PATTERN = Pattern.compile(".*(pr+|:3c?)$");

    private static void playMeowSound(String message, double x, double y, double z) {
        String raw = message.trim();
        if (raw.isEmpty()) return;
        var sound = SoundEvents.CAT_AMBIENT;
        if (raw.endsWith("!!")) sound = SoundEvents.CAT_HISS;
        else if (PURR_PATTERN.matcher(raw).matches()) sound = SoundEvents.CAT_PURR;
        var player = Minecraft.getInstance().player;
        if (player != null) player.level().playLocalSound(x, y, z, sound, SoundSource.PLAYERS, 1.0F, 1.0F, false);
    }

    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(ClientEvents.CONFIG_KEY);

        EntityModelLayerRegistry.registerModelLayer(CAT_EARS_LAYER, CatEarsModel::createBodyLayer);

        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, renderer, helper, ctx) -> {
            if (entityType == EntityType.PLAYER) {
                var model = new CatEarsModel<>(
                        Minecraft.getInstance().getEntityModels().bakeLayer(CAT_EARS_LAYER));
                helper.register(new CatEarsLayer(renderer, model));
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (ClientEvents.CONFIG_KEY.consumeClick()) {
                client.setScreen(new CatEarsConfigScreen(null));
            }
        });

        ClientSendMessageEvents.CHAT.register(message -> {
            if (serverHasMod) return;
            if (!CatEarsConfig.enableMeowing) return;
            var player = Minecraft.getInstance().player;
            if (player == null) return;
            playMeowSound(message, player.getX(), player.getY(), player.getZ());
        });

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            serverHasMod = ClientPlayNetworking.canSend(MeowConfigPayload.TYPE);
            if (serverHasMod) {
                try {
                    ClientPlayNetworking.send(new MeowConfigPayload(
                            CatEarsConfig.enableMeowing,
                            CatEarsConfig.showEarsLocally,
                            CatEarsConfig.earColor));
                } catch (Exception e) {
                    CatEarsMod.LOGGER.debug("Could not send meow config on login", e);
                }
            }
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            serverHasMod = false;
            remoteEarData.clear();
        });

        ClientPlayNetworking.registerGlobalReceiver(SyncEarDataPayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                remoteEarData.put(payload.playerUuid(), payload);
            });
        });
    }
}
