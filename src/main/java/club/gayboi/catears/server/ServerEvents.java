package club.gayboi.catears.server;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import club.gayboi.catears.CatEarsMod;
import club.gayboi.catears.ModItems;

public class ServerEvents {
    // per-player meow pref :3
    private static final Map<UUID, Boolean> playerMeowPreferences = new ConcurrentHashMap<>();
    // per-player max fall distance tracker :3
    private static final Map<UUID, Double> playerFallDistances = new ConcurrentHashMap<>();

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
        landingSound();
        disconnectCleanup();
    }

    private static void chatMeowSound() {
        ServerMessageEvents.CHAT_MESSAGE.register((message, sender, params) -> {
            if (!isWearingCatEars(sender)) return;

            // check meow enabled :3
            if (!isPlayerMeowEnabled(sender.getUUID())) return;

            // determine sound :3
            String rawText = message.signedBody().content().trim();
            var sound = new SoundEvent(Identifier.fromNamespaceAndPath("minecraft", "entity.cat.ambient"), Optional.empty());
            if (rawText.endsWith("!!")) {
                sound = new SoundEvent(Identifier.fromNamespaceAndPath("minecraft", "entity.cat.hiss"), Optional.empty());
            } else if (PURR_PATTERN.matcher(rawText).matches()) {
                sound = new SoundEvent(Identifier.fromNamespaceAndPath("minecraft", "entity.cat.purr"), Optional.empty());
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
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (!(entity instanceof ServerPlayer player)) return true;
            if (!isWearingCatEars(player)) return true;

            player.level().playSound(
                    null,
                    player.getX(), player.getY(), player.getZ(),
                    new SoundEvent(Identifier.fromNamespaceAndPath("minecraft", "entity.cat.hurt"), Optional.empty()),
                    SoundSource.PLAYERS,
                    1.0F, 1.0F
            );

            return true;
        });
    }

    private static void landingSound() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                UUID id = player.getUUID();
                if (!isWearingCatEars(player)) {
                    playerFallDistances.remove(id);
                    continue;
                }

                double currentFall = player.fallDistance;
                double trackedMax = playerFallDistances.getOrDefault(id, 0.0);

                if (player.onGround()) {
                    if (trackedMax >= 2.0) {
                        player.level().playSound(
                                null,
                                player.getX(), player.getY(), player.getZ(),
                                new SoundEvent(Identifier.fromNamespaceAndPath("minecraft", "entity.cat.ambient"), Optional.empty()),
                                SoundSource.PLAYERS,
                                1.0F, 1.0F
                        );
                    }
                    playerFallDistances.put(id, 0.0);
                } else {
                    playerFallDistances.put(id, Math.max(trackedMax, currentFall));
                }
            }
        });
    }

    private static void disconnectCleanup() {
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            playerMeowPreferences.remove(handler.player.getUUID());
            playerFallDistances.remove(handler.player.getUUID());
        });
    }
}
