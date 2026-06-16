package club.gayboi.catears;

import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.fabricmc.loader.api.FabricLoader;

public class CatEarsConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("catears.json");

    public static boolean enableMeowing = true;

    public static void load() {
        try {
            if (CONFIG_PATH.toFile().exists()) {
                try (FileReader reader = new FileReader(CONFIG_PATH.toFile())) {
                    ConfigData data = GSON.fromJson(reader, ConfigData.class);
                    if (data != null) {
                        enableMeowing = data.enableMeowing;
                    }
                }
            } else {
                save();
            }
        } catch (Exception e) {
            CatEarsMod.LOGGER.error("Failed to load config, using defaults", e);
        }
    }

    public static void save() {
        try {
            try (FileWriter writer = new FileWriter(CONFIG_PATH.toFile())) {
                GSON.toJson(new ConfigData(enableMeowing), writer);
            }
        } catch (Exception e) {
            CatEarsMod.LOGGER.error("Failed to save config", e);
        }
    }

    private static record ConfigData(boolean enableMeowing) {
        private ConfigData() {
            this(true);
        }
    }
}
