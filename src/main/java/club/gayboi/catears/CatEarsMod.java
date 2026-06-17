package club.gayboi.catears;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;

import club.gayboi.catears.network.MeowConfigPayload;

public class CatEarsMod implements ModInitializer {
    public static final String MOD_ID = "catears";
    public static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void onInitialize() {
        // load config :3
        CatEarsConfig.load();

        // register deferred registers :3
        ModArmorMaterials.register();
        ModItems.register();

        // creative tab :3
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
                Identifier.fromNamespaceAndPath(MOD_ID, "cat_ears_tab"),
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

        // register server events :3
        club.gayboi.catears.server.ServerEvents.register();

        // register network payloads :3
        PayloadTypeRegistry.playC2S().register(MeowConfigPayload.TYPE, MeowConfigPayload.STREAM_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(MeowConfigPayload.TYPE, (payload, context) -> {
            context.server().execute(() -> {
                var player = context.player();
                club.gayboi.catears.server.ServerEvents.setPlayerMeowEnabled(player.getUUID(), payload.enabled());
                LOGGER.debug("Player {} set meowing to {}", player.getName().getString(), payload.enabled());
            });
        });

        LOGGER.info("Cat Ears & Meows loaded! Meow~ :3");
    }
}
