package club.gayboi.catears.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import club.gayboi.catears.CatEarsConfig;
import club.gayboi.catears.network.MeowConfigPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class CatEarsConfigScreen extends Screen {
    private final Screen parent;

    public CatEarsConfigScreen(Screen parent) {
        super(Component.literal("Cat Ears & Meows"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        // meow toggle :3
        this.addRenderableWidget(Button.builder(
                getMeowButtonText(),
                button -> {
                    boolean newValue = !CatEarsConfig.enableMeowing;
                    CatEarsConfig.enableMeowing = newValue;
                    CatEarsConfig.save();
                    button.setMessage(getMeowButtonText());
                    // sync with server :3
                    if (this.minecraft != null && this.minecraft.getConnection() != null) {
                        try {
                            ClientPlayNetworking.send(new MeowConfigPayload(newValue));
                        } catch (Exception ignored) {
                        }
                    }
                }
        ).bounds(centerX - 100, centerY - 24, 200, 20).build());

        // done button :3
        this.addRenderableWidget(Button.builder(
                Component.literal("Done"),
                button -> this.onClose()
        ).bounds(centerX - 100, centerY + 8, 200, 20).build());
    }

    private static Component getMeowButtonText() {
        boolean enabled = CatEarsConfig.enableMeowing;
        return Component.literal("Enable Meowing: ").append(
                enabled
                        ? Component.literal("ON").withStyle(ChatFormatting.GREEN)
                        : Component.literal("OFF").withStyle(ChatFormatting.RED)
        );
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        // draw title :3
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, this.height / 2 - 48, 0xFFFFFF);
        // draw subtitle :3
        guiGraphics.drawCenteredString(this.font,
                Component.literal("by gayboi.club").withStyle(ChatFormatting.GRAY),
                this.width / 2, this.height / 2 - 36, 0xAAAAAA);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }
}
