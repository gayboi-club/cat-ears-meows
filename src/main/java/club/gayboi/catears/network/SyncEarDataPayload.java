package club.gayboi.catears.network;

import java.util.UUID;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;

import club.gayboi.catears.CatEarsMod;

public record SyncEarDataPayload(UUID playerUuid, boolean enabled, boolean showEars, String earColor) implements FabricPacket {
    public static final PacketType<SyncEarDataPayload> TYPE = PacketType.create(
            new ResourceLocation(CatEarsMod.MOD_ID, "sync_ear_data"),
            SyncEarDataPayload::read
    );

    private static SyncEarDataPayload read(FriendlyByteBuf buf) {
        return new SyncEarDataPayload(buf.readUUID(), buf.readBoolean(), buf.readBoolean(), buf.readUtf());
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(playerUuid);
        buf.writeBoolean(enabled);
        buf.writeBoolean(showEars);
        buf.writeUtf(earColor);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}
