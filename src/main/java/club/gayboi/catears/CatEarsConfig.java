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
    public static boolean showEarsLocally = true;
    public static String earColor = "white";

    public static void load() {
        try {
            if (CONFIG_PATH.toFile().exists()) {
                try (FileReader reader = new FileReader(CONFIG_PATH.toFile())) {
                    ConfigData data = GSON.fromJson(reader, ConfigData.class);
                    if (data != null) {
                        enableMeowing = data.enableMeowing;
                        if (data.earColor != null) {
                            showEarsLocally = data.showEarsLocally;
                            earColor = data.earColor;
                        } else {
                            showEarsLocally = true;
                            earColor = "white";
                            save();
                        }
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
                GSON.toJson(new ConfigData(enableMeowing, showEarsLocally, earColor), writer);
            }
        } catch (Exception e) {
            CatEarsMod.LOGGER.error("Failed to save config", e);
        }
    }

    private static record ConfigData(boolean enableMeowing, boolean showEarsLocally, String earColor) {
        private ConfigData() {
            this(true, true, "white");
        }
    }
}
