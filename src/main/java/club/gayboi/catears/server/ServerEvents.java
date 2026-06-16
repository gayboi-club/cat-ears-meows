package club.gayboi.catears.server;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import club.gayboi.catears.CatEarsMod;
import club.gayboi.catears.ModItems;

@EventBusSubscriber(modid = CatEarsMod.MOD_ID)
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
            if (helmet.is(catEarItem.get())) {
                return true;
            }
        }

        return false;
    }

    @SubscribeEvent
    public static void onChat(ServerChatEvent event) {
        ServerPlayer player = event.getPlayer();

        if (!isWearingCatEars(player)) return;

        // check meow enabled :3
        if (!isPlayerMeowEnabled(player.getUUID())) return;

        // determine sound :3
        String rawText = event.getMessage().getString().trim();
        var sound = SoundEvents.CAT_AMBIENT;
        if (rawText.endsWith("!!")) {
            sound = SoundEvents.CAT_HISS;
        } else if (PURR_PATTERN.matcher(rawText).matches()) {
            sound = SoundEvents.CAT_PURR;
        }

        // play sound for nearby :3
        player.level().playSound(
                null, // don't exclude :3
                player.getX(), player.getY(), player.getZ(),
                sound,
                SoundSource.PLAYERS,
                1.0F, 1.0F
        );
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        // cleanup prefs on logout :3
        playerMeowPreferences.remove(event.getEntity().getUUID());
    }
}
