package club.gayboi.catears.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import club.gayboi.catears.CatEarsMod;
import club.gayboi.catears.server.ServerEvents;

public record MeowConfigPayload(boolean enabled, boolean showEars, String earColor) implements CustomPacketPayload {
    public static final Type<MeowConfigPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(CatEarsMod.MOD_ID, "meow_config")
    );

    public static final StreamCodec<FriendlyByteBuf, MeowConfigPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.BOOL, MeowConfigPayload::enabled,
                    ByteBufCodecs.BOOL, MeowConfigPayload::showEars,
                    ByteBufCodecs.STRING_UTF8, MeowConfigPayload::earColor,
                    MeowConfigPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(MeowConfigPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                ServerEvents.setPlayerEarData(
                        serverPlayer.getUUID(), payload.enabled(), payload.showEars(), payload.earColor());
                CatEarsMod.LOGGER.debug("Player {} set ear data enabled={} showEars={} color={}",
                        serverPlayer.getName().getString(), payload.enabled(), payload.showEars(), payload.earColor());

                var syncPayload = new SyncEarDataPayload(
                        serverPlayer.getUUID(), payload.enabled(), payload.showEars(), payload.earColor());
                for (ServerPlayer other : serverPlayer.getServer().getPlayerList().getPlayers()) {
                    if (!other.getUUID().equals(serverPlayer.getUUID())) {
                        PacketDistributor.sendToPlayer(other, syncPayload);
                    }
                }
            }
        });
    }
}
