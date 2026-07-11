package club.gayboi.catears.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityRenderLayerRegistrationCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.player.Player;

import club.gayboi.catears.CatEarsConfig;
import club.gayboi.catears.CatEarsMod;
import club.gayboi.catears.client.model.CatEarsModel;
import club.gayboi.catears.client.renderer.CatEarsLayer;
import club.gayboi.catears.network.MeowConfigPayload;
import club.gayboi.catears.network.SyncEarDataPayload;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class CatEarsClientMod implements ClientModInitializer {
    public static final ModelLayerLocation CAT_EARS_LAYER = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(CatEarsMod.MOD_ID, "cat_ears"), "main");

    public static boolean serverHasMod = false;

    public record EarData(boolean enabled, boolean showEars, String earColor, long timestamp) {}

    public static final Map<UUID, EarData> remoteEarData = new ConcurrentHashMap<>();

    private static final Pattern PURR_PATTERN = Pattern.compile(".*(pr+|:3c?)$");
    private static final long HURT_COOLDOWN_MS = 200L;

    private static float lastHealth = Float.NaN;
    private static long lastHurtSoundTime = 0L;

    private static SoundEvent catAmbient() {
        return new SoundEvent(Identifier.fromNamespaceAndPath("minecraft", "entity.cat.ambient"), Optional.empty());
    }

    private static SoundEvent catHiss() {
        return new SoundEvent(Identifier.fromNamespaceAndPath("minecraft", "entity.cat.hiss"), Optional.empty());
    }

    private static SoundEvent catPurr() {
        return new SoundEvent(Identifier.fromNamespaceAndPath("minecraft", "entity.cat.purr"), Optional.empty());
    }

    private static SoundEvent catHurt() {
        return new SoundEvent(Identifier.fromNamespaceAndPath("minecraft", "entity.cat.hurt"), Optional.empty());
    }

    private static void playSoundAt(double x, double y, double z, SoundEvent sound) {
        var player = Minecraft.getInstance().player;
        if (player != null) {
            player.level().playLocalSound(x, y, z, sound, SoundSource.PLAYERS, 1.0F, 1.0F, false);
        }
    }

    private static void playMeowSound(String message, double x, double y, double z) {
        if (!CatEarsConfig.enableMeowing) return;

        String raw = message.trim();
        if (raw.isEmpty()) return;

        SoundEvent sound = catAmbient();
        if (raw.endsWith("!!")) {
            sound = catHiss();
        } else if (PURR_PATTERN.matcher(raw).matches()) {
            sound = catPurr();
        }

        playSoundAt(x, y, z, sound);
    }

    @Override
    public void onInitializeClient() {
        KeyMappingHelper.registerKeyMapping(ClientEvents.CONFIG_KEY);

        ModelLayerRegistry.registerModelLayer(CAT_EARS_LAYER, CatEarsModel::createBodyLayer);

        LivingEntityRenderLayerRegistrationCallback.EVENT.register((entityType, renderer, helper, context) -> {
            CatEarsMod.LOGGER.info("Layer registration callback fired for entityType={}", entityType);
            if (entityType == EntityTypes.PLAYER) {
                CatEarsMod.LOGGER.info("Registering CatEarsLayer for PLAYER, renderer={}", renderer.getClass().getName());
                var model = new CatEarsModel(
                        Minecraft.getInstance().getEntityModels().bakeLayer(CAT_EARS_LAYER));
                var layer = new CatEarsLayer(renderer, model);
                helper.register(layer);
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (ClientEvents.CONFIG_KEY.consumeClick()) {
                client.setScreenAndShow(new CatEarsConfigScreen(null));
            }

            if (!CatEarsConfig.showEarsLocally) {
                lastHealth = Float.NaN;
                return;
            }

            var player = client.player;
            if (player == null) return;

            float health = player.getHealth();
            if (!Float.isNaN(lastHealth) && health < lastHealth) {
                long now = System.currentTimeMillis();
                if (now - lastHurtSoundTime >= HURT_COOLDOWN_MS) {
                    lastHurtSoundTime = now;
                    playSoundAt(player.getX(), player.getY(), player.getZ(), catHurt());
                }
            }
            lastHealth = health;
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
            lastHealth = Float.NaN;
            lastHurtSoundTime = 0L;
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            serverHasMod = false;
            lastHealth = Float.NaN;
            lastHurtSoundTime = 0L;
            remoteEarData.clear();
        });

        ClientPlayNetworking.registerGlobalReceiver(SyncEarDataPayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                remoteEarData.put(payload.playerUuid(), new EarData(
                        payload.enabled(), payload.showEars(), payload.earColor(),
                        System.currentTimeMillis()));
            });
        });
    }
}
