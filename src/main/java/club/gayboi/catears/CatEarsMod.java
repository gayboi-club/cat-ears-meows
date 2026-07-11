package club.gayboi.catears;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.DyeColor;

import club.gayboi.catears.network.MeowConfigPayload;
import club.gayboi.catears.network.SyncEarDataPayload;

public class CatEarsMod implements ModInitializer {
    public static final String MOD_ID = "catears";
    public static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void onInitialize() {
        CatEarsConfig.load();

        ModArmorMaterials.register();
        ModItems.register();

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "cat_ears_tab"),
                FabricItemGroup.builder()
                        .title(Component.translatable("itemGroup.catears"))
                        .icon(() -> ModItems.CAT_EARS.get(DyeColor.WHITE).getDefaultInstance())
                        .displayItems((params, output) -> {
                            for (DyeColor color : DyeColor.values()) {
                                var item = ModItems.CAT_EARS.get(color);
                                if (item != null) {
                                    output.accept(item);
                                }
                            }
                        })
                        .build()
        );

        club.gayboi.catears.server.ServerEvents.register();

        PayloadTypeRegistry.playC2S().register(MeowConfigPayload.TYPE, MeowConfigPayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(SyncEarDataPayload.TYPE, SyncEarDataPayload.STREAM_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(MeowConfigPayload.TYPE, (payload, context) -> {
            context.server().execute(() -> {
                var player = context.player();
                club.gayboi.catears.server.ServerEvents.setPlayerEarData(
                        player.getUUID(), payload.enabled(), payload.showEars(), payload.earColor());
                LOGGER.debug("Player {} set ear data enabled={} showEars={} color={}",
                        player.getName().getString(), payload.enabled(), payload.showEars(), payload.earColor());

                var syncPayload = new SyncEarDataPayload(
                        player.getUUID(), payload.enabled(), payload.showEars(), payload.earColor());
                for (ServerPlayer other : PlayerLookup.all(context.server())) {
                    if (!other.getUUID().equals(player.getUUID())) {
                        ServerPlayNetworking.send(other, syncPayload);
                    }
                }
            });
        });

        LOGGER.info("Cat Ears & Meows loaded! Meow~ :3");
    }
}
