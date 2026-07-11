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
import net.neoforged.neoforge.network.PacketDistributor;

import club.gayboi.catears.CatEarsMod;
import club.gayboi.catears.ModItems;
import club.gayboi.catears.network.SyncEarDataPayload;

@EventBusSubscriber(modid = CatEarsMod.MOD_ID)
public class ServerEvents {
    public record EarData(boolean enabled, boolean showEars, String earColor) {}

    private static final Map<UUID, EarData> playerEarData = new ConcurrentHashMap<>();

    public static void setPlayerEarData(UUID playerId, boolean enabled, boolean showEars, String earColor) {
        playerEarData.put(playerId, new EarData(enabled, showEars, earColor));
    }

    public static boolean isPlayerMeowEnabled(UUID playerId) {
        EarData data = playerEarData.get(playerId);
        return data == null || data.enabled();
    }

    private static final java.util.regex.Pattern PURR_PATTERN = java.util.regex.Pattern.compile(".*(pr+|:3c?)$");

    private static boolean isWearingCatEars(ServerPlayer player) {
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

        if (!isPlayerMeowEnabled(player.getUUID())) return;

        String rawText = event.getMessage().getString().trim();
        var sound = SoundEvents.CAT_AMBIENT;
        if (rawText.endsWith("!!")) {
            sound = SoundEvents.CAT_HISS;
        } else if (PURR_PATTERN.matcher(rawText).matches()) {
            sound = SoundEvents.CAT_PURR;
        }

        player.level().playSound(
                null,
                player.getX(), player.getY(), player.getZ(),
                sound,
                SoundSource.PLAYERS,
                1.0F, 1.0F
        );
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer newPlayer) {
            for (Map.Entry<UUID, EarData> entry : playerEarData.entrySet()) {
                UUID uuid = entry.getKey();
                EarData data = entry.getValue();
                PacketDistributor.sendToPlayer(newPlayer, new SyncEarDataPayload(
                        uuid, data.enabled(), data.showEars(), data.earColor()));
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        playerEarData.remove(event.getEntity().getUUID());
    }
}
