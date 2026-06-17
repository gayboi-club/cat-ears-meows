package club.gayboi.catears.client;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;

import org.lwjgl.glfw.GLFW;

public class ClientEvents {
    public static final KeyMapping CONFIG_KEY = new KeyMapping(
            "key.catears.config",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_K,
            KeyMapping.Category.MISC
    );
}
