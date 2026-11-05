package club.gayboi.catears;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;

import club.gayboi.catears.server.ServerEvents;

public class CatEarsMod implements ModInitializer {
    public static final String MOD_ID = "catears";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModItems.register();

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
                Identifier.fromNamespaceAndPath(MOD_ID, "cat_ears_tab"),
                FabricCreativeModeTab.builder()
                        .title(Component.translatable("itemGroup.catears"))
                        .icon(() -> ModItems.CAT_EARS.get(DyeColor.WHITE).getDefaultInstance())
                        .displayItems((params, output) -> ModItems.CAT_EARS.values().forEach(output::accept))
                        .build()
        );

        ServerEvents.register();

        LOGGER.info("Cat Ears & Meows loaded! Meow~ :3");
    }
}
