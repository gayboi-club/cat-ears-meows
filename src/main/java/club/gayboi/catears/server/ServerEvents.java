package club.gayboi.catears.server;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.feline.CatSoundVariant;
import net.minecraft.world.entity.animal.feline.CatSoundVariants;

import club.gayboi.catears.ModItems;

public final class ServerEvents {
    private static final CatSoundVariant.CatSoundSet CAT =
            SoundEvents.CAT_SOUNDS.get(CatSoundVariants.SoundSet.CLASSIC).adultSounds();

    private static final Pattern PURR_PATTERN = Pattern.compile("(?:\\bp+r+|:3c?)~*$", Pattern.CASE_INSENSITIVE);

    private static final int HURT_MEW_COOLDOWN_TICKS = 4;
    private static final Map<UUID, Integer> lastHurtMewTick = new ConcurrentHashMap<>();

    private ServerEvents() {}

    public static void register() {
        ServerMessageEvents.CHAT_MESSAGE.register((message, sender, params) -> {
            if (!isWearingCatEars(sender)) return;

            String text = message.signedBody().content().trim();
            Holder<SoundEvent> sound;
            if (text.endsWith("!!")) {
                sound = CAT.hissSound();
            } else if (PURR_PATTERN.matcher(text).find()) {
                sound = CAT.purrSound();
            } else {
                sound = CAT.ambientSound();
            }
            playCatSound(sender, sound);
        });

        ServerLivingEntityEvents.AFTER_DAMAGE.register((entity, source, baseDamageTaken, damageTaken, blocked) -> {
            if (blocked || damageTaken <= 0.0F) return;
            if (!(entity instanceof ServerPlayer player) || !isWearingCatEars(player)) return;

            int now = player.level().getServer().getTickCount();
            Integer last = lastHurtMewTick.get(player.getUUID());
            if (last != null && now - last < HURT_MEW_COOLDOWN_TICKS) return;

            lastHurtMewTick.put(player.getUUID(), now);
            playCatSound(player, CAT.hurtSound());
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) ->
                lastHurtMewTick.remove(handler.player.getUUID()));
    }

    private static boolean isWearingCatEars(ServerPlayer player) {
        return ModItems.isCatEars(player.getItemBySlot(EquipmentSlot.HEAD));
    }

    private static void playCatSound(ServerPlayer player, Holder<SoundEvent> sound) {
        RandomSource random = player.getRandom();
        float pitch = 1.0F + (random.nextFloat() - random.nextFloat()) * 0.2F;
        player.level().playSound(
                null,
                player.getX(), player.getY(), player.getZ(),
                sound,
                SoundSource.PLAYERS,
                1.0F, pitch
        );
    }
}
