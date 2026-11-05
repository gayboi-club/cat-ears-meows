package club.gayboi.catears;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.ArmorType;

public final class ModItems {
    private static final Map<DyeColor, Item> CAT_EARS_BY_COLOR = new EnumMap<>(DyeColor.class);

    public static final Map<DyeColor, Item> CAT_EARS = Collections.unmodifiableMap(CAT_EARS_BY_COLOR);

    private ModItems() {}

    public static void register() {
        for (DyeColor color : DyeColor.values()) {
            ResourceKey<Item> itemKey = ResourceKey.create(
                    Registries.ITEM,
                    Identifier.fromNamespaceAndPath(CatEarsMod.MOD_ID, color.getName() + "_cat_ears")
            );
            Item item = Registry.register(
                    BuiltInRegistries.ITEM,
                    itemKey,
                    new Item(
                            new Item.Properties()
                                    .humanoidArmor(ModArmorMaterials.CAT_EARS, ArmorType.HELMET)
                                    .setId(itemKey)
                    )
            );
            CAT_EARS_BY_COLOR.put(color, item);
        }
    }

    public static boolean isCatEars(ItemStack stack) {
        return !stack.isEmpty() && CAT_EARS_BY_COLOR.containsValue(stack.getItem());
    }
}
