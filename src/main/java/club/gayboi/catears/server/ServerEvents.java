package club.gayboi.catears.server;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import club.gayboi.catears.CatEarsMod;
import club.gayboi.catears.ModItems;
import club.gayboi.catears.network.SyncEarDataPayload;

public class ServerEvents {
    public static record EarData(boolean enabled, boolean showEars, String earColor) {}

    private static final Map<UUID, EarData> playerEarData = new ConcurrentHashMap<>();
    private static final Map<UUID, Long> lastHurtSoundTime = new ConcurrentHashMap<>();
    private static final Map<UUID, Float> playerHealths = new ConcurrentHashMap<>();

    public static void setPlayerEarData(UUID playerId, boolean enabled, boolean showEars, String earColor) {
        playerEarData.put(playerId, new EarData(enabled, showEars, earColor));
    }

    public static boolean isPlayerMeowEnabled(UUID playerId) {
        EarData data = playerEarData.get(playerId);
        return data == null || data.enabled;
    }

    public static EarData getPlayerEarData(UUID playerId) {
        return playerEarData.get(playerId);
    }

    public static void syncOnJoin() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                EarData data = getPlayerEarData(player.getUUID());
                if (data != null) {
                    ServerPlayNetworking.send(player, new SyncEarDataPayload(
                            player.getUUID(), data.enabled, data.showEars, data.earColor));
                    for (ServerPlayer other : PlayerLookup.all(server)) {
                        if (other != player) {
                            ServerPlayNetworking.send(player, new SyncEarDataPayload(
                                    other.getUUID(),
                                    getPlayerEarData(other.getUUID()) != null ? getPlayerEarData(other.getUUID()).enabled : true,
                                    getPlayerEarData(other.getUUID()) != null ? getPlayerEarData(other.getUUID()).showEars : true,
                                    getPlayerEarData(other.getUUID()) != null ? getPlayerEarData(other.getUUID()).earColor : "white"));
                        }
                    }
                }
            }
        });
    }

    private static final java.util.regex.Pattern PURR_PATTERN = java.util.regex.Pattern.compile(".*(pr+|:3c?)$");

    private static boolean isWearingCatEars(ServerPlayer player) {
        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        for (var catEarItem : ModItems.CAT_EARS.values()) {
            if (helmet.is(catEarItem)) {
                return true;
            }
        }

        return false;
    }

    public static void register() {
        chatMeowSound();
        hurtSound();
        disconnectCleanup();
    }

    private static void chatMeowSound() {
        ServerMessageEvents.CHAT_MESSAGE.register((message, sender, params) -> {
            if (!isWearingCatEars(sender)) return;

            if (!isPlayerMeowEnabled(sender.getUUID())) return;

            String rawText = message.signedBody().content().trim();
            var sound = SoundEvents.CAT_AMBIENT;
            if (rawText.endsWith("!!")) {
                sound = SoundEvents.CAT_HISS;
            } else if (PURR_PATTERN.matcher(rawText).matches()) {
                sound = SoundEvents.CAT_PURR;
            }

            sender.level().playSound(
                    null,
                    sender.getX(), sender.getY(), sender.getZ(),
                    sound,
                    SoundSource.PLAYERS,
                    1.0F, 1.0F
            );
        });
    }

    private static void hurtSound() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                if (!isWearingCatEars(player)) continue;

                UUID id = player.getUUID();
                float prevHealth = playerHealths.getOrDefault(id, player.getHealth());
                float currentHealth = player.getHealth();

                if (currentHealth < prevHealth) {
                    long now = System.currentTimeMillis();
                    long last = lastHurtSoundTime.getOrDefault(id, 0L);
                    if (now - last >= 200) {
                        lastHurtSoundTime.put(id, now);
                        player.level().playSound(
                                null,
                                player.getX(), player.getY(), player.getZ(),
                                SoundEvents.CAT_HURT,
                                SoundSource.PLAYERS,
                                1.0F, 1.0F
                        );
                    }
                }

                playerHealths.put(id, currentHealth);
            }
        });
    }

    private static void disconnectCleanup() {
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            playerEarData.remove(handler.player.getUUID());
            playerHealths.remove(handler.player.getUUID());
            lastHurtSoundTime.remove(handler.player.getUUID());
        });
    }
}
