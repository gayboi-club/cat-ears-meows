package club.gayboi.catears.server;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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

        // check trinkets slot :3
        if (net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("trinkets")) {
            return TrinketsCompat.hasCatEars(player);
        }

        return false;
    }

    private static class TrinketsCompat {
        static boolean hasCatEars(ServerPlayer player) {
            try {
                Class<?> trinketsApi = Class.forName("dev.emi.trinkets.api.TrinketsApi");
                var inventory = trinketsApi.getMethod("getTrinketComponent", net.minecraft.world.entity.LivingEntity.class)
                        .invoke(null, player);
                if (inventory == null) return false;

                var invClass = Class.forName("dev.emi.trinkets.api.TrinketComponent");
                var isEquipped = invClass.getMethod("isEquipped", net.minecraft.world.item.Item.class);

                for (var catEarItem : ModItems.CAT_EARS.values()) {
                    if ((boolean) isEquipped.invoke(inventory, catEarItem)) {
                        return true;
                    }
                }
            } catch (Exception e) { // ignore errors :3
            }
            return false;
        }
    }

    public static void register() {
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

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            // cleanup prefs on logout :3
            playerMeowPreferences.remove(handler.player.getUUID());
        });
    }
}
