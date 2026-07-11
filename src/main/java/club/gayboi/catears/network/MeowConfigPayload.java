package club.gayboi.catears.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;

import club.gayboi.catears.CatEarsMod;

public record MeowConfigPayload(boolean enabled, boolean showEars, String earColor) implements FabricPacket {
    public static final PacketType<MeowConfigPayload> TYPE = PacketType.create(
            new ResourceLocation(CatEarsMod.MOD_ID, "meow_config"),
            MeowConfigPayload::read
    );

    private static MeowConfigPayload read(FriendlyByteBuf buf) {
        return new MeowConfigPayload(buf.readBoolean(), buf.readBoolean(), buf.readUtf());
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBoolean(enabled);
        buf.writeBoolean(showEars);
        buf.writeUtf(earColor);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}
