package club.gayboi.catears;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import club.gayboi.catears.client.ClientEvents;
import club.gayboi.catears.network.MeowConfigPayload;

@Mod(CatEarsMod.MOD_ID)
public class CatEarsMod {
    public static final String MOD_ID = "catears";
    public static final Logger LOGGER = LogUtils.getLogger();

    // creative tab :3
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CAT_EARS_TAB =
            CREATIVE_TABS.register("cat_ears_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.catears"))
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> ModItems.CAT_EARS.get(DyeColor.WHITE).get().getDefaultInstance())
                    .displayItems((params, output) -> {
                        for (DyeColor color : DyeColor.values()) {
                            var item = ModItems.CAT_EARS.get(color);
                            if (item != null) {
                                output.accept(item.get());
                            }
                        }
                    })
                    .build()
            );

    public CatEarsMod(IEventBus modEventBus, ModContainer modContainer) {
        // register deferred registers :3
        ModArmorMaterials.ARMOR_MATERIALS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        CREATIVE_TABS.register(modEventBus);

        // register network payloads :3
        modEventBus.addListener(this::registerPayloads);

        // register client config :3
        modContainer.registerConfig(ModConfig.Type.CLIENT, CatEarsConfig.SPEC);

        LOGGER.info("Cat Ears & Meows loaded! Meow~ :3");
    }

    private void registerPayloads(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(MOD_ID);
        registrar.playToServer(
                MeowConfigPayload.TYPE,
                MeowConfigPayload.STREAM_CODEC,
                MeowConfigPayload::handle
        );
    }

    // client mod bus events :3
    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void registerKeys(RegisterKeyMappingsEvent event) {
            event.register(ClientEvents.CONFIG_KEY);
        }
    }
}
