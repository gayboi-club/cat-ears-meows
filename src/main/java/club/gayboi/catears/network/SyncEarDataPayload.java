package club.gayboi.catears.network;

import java.util.UUID;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import club.gayboi.catears.CatEarsMod;

public record SyncEarDataPayload(UUID playerUuid, boolean enabled, boolean showEars, String earColor) implements CustomPacketPayload {
    public static final Type<SyncEarDataPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(CatEarsMod.MOD_ID, "sync_ear_data")
    );

    public static final StreamCodec<FriendlyByteBuf, UUID> UUID_CODEC =
            StreamCodec.of(
                (buf, uuid) -> buf.writeUUID(uuid),
                buf -> buf.readUUID());

    public static final StreamCodec<FriendlyByteBuf, SyncEarDataPayload> STREAM_CODEC =
            StreamCodec.composite(
                    UUID_CODEC, SyncEarDataPayload::playerUuid,
                    ByteBufCodecs.BOOL, SyncEarDataPayload::enabled,
                    ByteBufCodecs.BOOL, SyncEarDataPayload::showEars,
                    ByteBufCodecs.STRING_UTF8, SyncEarDataPayload::earColor,
                    SyncEarDataPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
