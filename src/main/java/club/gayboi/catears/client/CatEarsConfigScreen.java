package club.gayboi.catears.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;

import club.gayboi.catears.CatEarsConfig;
import club.gayboi.catears.network.MeowConfigPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import java.util.Arrays;
import java.util.List;

public class CatEarsConfigScreen extends Screen {
    private final Screen parent;

    private static final List<DyeColor> COLORS = Arrays.asList(DyeColor.values());
    private int colorIndex;

    public CatEarsConfigScreen(Screen parent) {
        super(Component.literal("Cat Ears & Meows"));
        this.parent = parent;
        DyeColor current = DyeColor.byName(CatEarsConfig.earColor, DyeColor.WHITE);
        this.colorIndex = COLORS.indexOf(current);
        if (this.colorIndex < 0) this.colorIndex = 0;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        this.addRenderableWidget(Button.builder(
                getMeowButtonText(),
                button -> {
                    CatEarsConfig.enableMeowing = !CatEarsConfig.enableMeowing;
                    CatEarsConfig.save();
                    button.setMessage(getMeowButtonText());
                    sendConfig();
                }
        ).bounds(centerX - 100, centerY - 36, 200, 20).build());

        this.addRenderableWidget(Button.builder(
                getEarsButtonText(),
                button -> {
                    CatEarsConfig.showEarsLocally = !CatEarsConfig.showEarsLocally;
                    CatEarsConfig.save();
                    button.setMessage(getEarsButtonText());
                    sendConfig();
                }
        ).bounds(centerX - 100, centerY - 12, 200, 20).build());

        this.addRenderableWidget(Button.builder(
                getColorButtonText(),
                button -> {
                    colorIndex = (colorIndex + 1) % COLORS.size();
                    CatEarsConfig.earColor = COLORS.get(colorIndex).getName();
                    CatEarsConfig.save();
                    button.setMessage(getColorButtonText());
                    sendConfig();
                }
        ).bounds(centerX - 100, centerY + 12, 200, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("Done"),
                button -> this.onClose()
        ).bounds(centerX - 100, centerY + 40, 200, 20).build());
    }

    private static Component getMeowButtonText() {
        return Component.literal("Enable Meowing: ").append(
                CatEarsConfig.enableMeowing
                        ? Component.literal("ON").withStyle(ChatFormatting.GREEN)
                        : Component.literal("OFF").withStyle(ChatFormatting.RED)
        );
    }

    private static Component getEarsButtonText() {
        return Component.literal("Show Cat Ears: ").append(
                CatEarsConfig.showEarsLocally
                        ? Component.literal("ON").withStyle(ChatFormatting.GREEN)
                        : Component.literal("OFF").withStyle(ChatFormatting.RED)
        );
    }

    private Component getColorButtonText() {
        DyeColor color = COLORS.get(colorIndex);
        String name = color.getName().substring(0, 1).toUpperCase() + color.getName().substring(1);
        int hex = color.getTextureDiffuseColor();
        return Component.literal("Ear Color: ").append(
                Component.literal(name).withColor(hex)
        );
    }

    private void sendConfig() {
        if (this.minecraft != null && this.minecraft.getConnection() != null) {
            try {
                ClientPlayNetworking.send(new MeowConfigPayload(
                        CatEarsConfig.enableMeowing,
                        CatEarsConfig.showEarsLocally,
                        CatEarsConfig.earColor));
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, this.height / 2 - 60, 0xFFFFFF);
        guiGraphics.drawCenteredString(this.font,
                Component.literal("by gayboi.club").withStyle(ChatFormatting.GRAY),
                this.width / 2, this.height / 2 - 48, 0xAAAAAA);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }
}
