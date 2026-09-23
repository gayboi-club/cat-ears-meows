package club.gayboi.catears;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;

public class CatEarsMod implements ModInitializer {
    public static final String MOD_ID = "catears";
    public static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void onInitialize() {
        ModArmorMaterials.register();
        ModItems.register();

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
                Identifier.fromNamespaceAndPath(MOD_ID, "cat_ears_tab"),
                FabricCreativeModeTab.builder()
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

        LOGGER.info("Cat Ears & Meows loaded! Meow~ :3");
    }
}