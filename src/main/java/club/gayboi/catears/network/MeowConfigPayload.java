package club.gayboi.catears.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import club.gayboi.catears.CatEarsMod;

public record MeowConfigPayload(boolean enabled) implements CustomPacketPayload {
    public static final Type<MeowConfigPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(CatEarsMod.MOD_ID, "meow_config")
    );

    public static final StreamCodec<FriendlyByteBuf, MeowConfigPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.BOOL, MeowConfigPayload::enabled,
                    MeowConfigPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
