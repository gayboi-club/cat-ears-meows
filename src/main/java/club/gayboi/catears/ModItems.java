package club.gayboi.catears;

import java.util.EnumMap;
import java.util.Map;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;

public class ModItems {
    // all 16 color variants :3
    public static final Map<DyeColor, Item> CAT_EARS = new EnumMap<>(DyeColor.class);

    public static void register() {
        for (DyeColor color : DyeColor.values()) {
            String name = color.getName() + "_cat_ears";
            Item item = Registry.register(
                    BuiltInRegistries.ITEM,
                    ResourceLocation.fromNamespaceAndPath(CatEarsMod.MOD_ID, name),
                    new ArmorItem(
                            ModArmorMaterials.CAT_EARS,
                            ArmorItem.Type.HELMET,
                            new Item.Properties().durability(55)
                    )
            );
            CAT_EARS.put(color, item);
        }
    }
}
