package club.gayboi.catears;

import net.neoforged.neoforge.common.ModConfigSpec;

public class CatEarsConfig {
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.BooleanValue ENABLE_MEOWING;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.comment("Cat Ears & Meows Configuration");
        builder.push("general");

        ENABLE_MEOWING = builder
                .comment("Whether wearing cat ears makes you meow when sending chat messages.",
                         "Set to false to disable meowing sounds.")
                .define("enableMeowing", true);

        builder.pop();

        SPEC = builder.build();
    }
}
