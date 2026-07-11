package club.gayboi.catears;

import net.neoforged.neoforge.common.ModConfigSpec;

public class CatEarsConfig {
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.BooleanValue ENABLE_MEOWING;
    public static final ModConfigSpec.BooleanValue SHOW_EARS_LOCALLY;
    public static final ModConfigSpec.ConfigValue<String> EAR_COLOR;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.comment("Cat Ears & Meows Configuration");
        builder.push("general");

        ENABLE_MEOWING = builder
                .comment("Whether wearing cat ears makes you meow when sending chat messages.",
                         "Set to false to disable meowing sounds.")
                .define("enableMeowing", true);

        SHOW_EARS_LOCALLY = builder
                .comment("Whether to show cat ears on your own character.",
                         "Set to false to hide them.")
                .define("showEarsLocally", true);

        EAR_COLOR = builder
                .comment("The color of cat ears to show on your character.",
                         "One of: white, orange, magenta, light_blue, yellow, lime, pink, gray, light_gray, cyan, purple, blue, brown, green, red, black")
                .define("earColor", "white");

        builder.pop();

        SPEC = builder.build();
    }
}
