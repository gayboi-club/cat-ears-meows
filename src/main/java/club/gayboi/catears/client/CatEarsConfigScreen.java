package club.gayboi.catears.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import club.gayboi.catears.CatEarsConfig;
import club.gayboi.catears.network.MeowConfigPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class CatEarsConfigScreen extends Screen {
    private final Screen parent;

    private static final String[] COLORS = {
            "white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray",
            "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black"
    };

    public CatEarsConfigScreen(Screen parent) {
        super(Component.literal("Cat Ears & Meows"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        this.addRenderableWidget(Button.builder(
                getMeowButtonText(),
                button -> {
                    boolean newValue = !CatEarsConfig.enableMeowing;
                    CatEarsConfig.enableMeowing = newValue;
                    CatEarsConfig.save();
                    button.setMessage(getMeowButtonText());
                    sendConfig();
                }
        ).bounds(centerX - 100, centerY - 54, 200, 20).build());

        this.addRenderableWidget(Button.builder(
                getShowEarsButtonText(),
                button -> {
                    boolean newValue = !CatEarsConfig.showEarsLocally;
                    CatEarsConfig.showEarsLocally = newValue;
                    CatEarsConfig.save();
                    button.setMessage(getShowEarsButtonText());
                    sendConfig();
                }
        ).bounds(centerX - 100, centerY - 24, 200, 20).build());

        this.addRenderableWidget(Button.builder(
                getColorButtonText(),
                button -> {
                    String currentColor = CatEarsConfig.earColor;
                    int idx = -1;
                    for (int i = 0; i < COLORS.length; i++) {
                        if (COLORS[i].equals(currentColor)) {
                            idx = i;
                            break;
                        }
                    }
                    idx = (idx + 1) % COLORS.length;
                    CatEarsConfig.earColor = COLORS[idx];
                    CatEarsConfig.save();
                    button.setMessage(getColorButtonText());
                    sendConfig();
                }
        ).bounds(centerX - 100, centerY + 6, 200, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("Done"),
                button -> this.onClose()
        ).bounds(centerX - 100, centerY + 36, 200, 20).build());
    }

    private void sendConfig() {
        if (this.minecraft != null && this.minecraft.getConnection() != null) {
            try {
                ClientPlayNetworking.send(new MeowConfigPayload(
                        CatEarsConfig.enableMeowing, CatEarsConfig.showEarsLocally, CatEarsConfig.earColor));
            } catch (Exception ignored) {
            }
        }
    }

    private static Component getMeowButtonText() {
        boolean enabled = CatEarsConfig.enableMeowing;
        return Component.literal("Enable Meowing: ").append(
                enabled
                        ? Component.literal("ON").withStyle(ChatFormatting.GREEN)
                        : Component.literal("OFF").withStyle(ChatFormatting.RED)
        );
    }

    private static Component getShowEarsButtonText() {
        boolean enabled = CatEarsConfig.showEarsLocally;
        return Component.literal("Show Ears: ").append(
                enabled
                        ? Component.literal("ON").withStyle(ChatFormatting.GREEN)
                        : Component.literal("OFF").withStyle(ChatFormatting.RED)
        );
    }

    private static Component getColorButtonText() {
        String color = CatEarsConfig.earColor;
        return Component.literal("Ear Color: " + color.substring(0, 1).toUpperCase() + color.substring(1));
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphicsExtractor, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(guiGraphicsExtractor, mouseX, mouseY, partialTick);
        guiGraphicsExtractor.centeredText(this.font, this.title, this.width / 2, this.height / 2 - 78, 0xFFFFFFFF);
        guiGraphicsExtractor.centeredText(this.font,
                Component.literal("by gayboi.club").withStyle(ChatFormatting.GRAY),
                this.width / 2, this.height / 2 - 66, 0xFFAAAAAA);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }
}
