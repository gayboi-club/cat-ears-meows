package club.gayboi.catears.server;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import club.gayboi.catears.CatEarsMod;
import club.gayboi.catears.ModItems;

public class ServerEvents {
    // per-player meow pref :3
    private static final Map<UUID, Boolean> playerMeowPreferences = new ConcurrentHashMap<>();
    // per-player hurt sound debounce :3
    private static final Map<UUID, Long> lastHurtSoundTime = new ConcurrentHashMap<>();
    // per-player health for detecting damage :3
    private static final Map<UUID, Float> playerHealths = new ConcurrentHashMap<>();

    public static void setPlayerMeowEnabled(UUID playerId, boolean enabled) {
        playerMeowPreferences.put(playerId, enabled);
    }

    public static boolean isPlayerMeowEnabled(UUID playerId) {
        return playerMeowPreferences.getOrDefault(playerId, true);
    }

    private static final java.util.regex.Pattern PURR_PATTERN = java.util.regex.Pattern.compile(".*(pr+|:3c?)$");

    private static boolean isWearingCatEars(ServerPlayer player) {
        // check helmet slot :3
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

            // check meow enabled :3
            if (!isPlayerMeowEnabled(sender.getUUID())) return;

            // determine sound :3
            String rawText = message.signedBody().content().trim();
            var sound = SoundEvents.CAT_AMBIENT;
            if (rawText.endsWith("!!")) {
                sound = SoundEvents.CAT_HISS;
            } else if (PURR_PATTERN.matcher(rawText).matches()) {
                sound = SoundEvents.CAT_PURR;
            }

            // play sound for nearby :3
            sender.level().playSound(
                    null, // don't exclude :3
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
            playerMeowPreferences.remove(handler.player.getUUID());
            playerHealths.remove(handler.player.getUUID());
            lastHurtSoundTime.remove(handler.player.getUUID());
        });
    }
}
