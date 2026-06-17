package club.gayboi.catears;

import java.util.EnumMap;
import java.util.Map;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorType;

public class ModItems {
    // all 16 color variants :3
    public static final Map<DyeColor, Item> CAT_EARS = new EnumMap<>(DyeColor.class);

    public static void register() {
        for (DyeColor color : DyeColor.values()) {
            String name = color.getName() + "_cat_ears";
            ResourceKey<Item> itemKey = ResourceKey.create(
                    Registries.ITEM,
                    Identifier.fromNamespaceAndPath(CatEarsMod.MOD_ID, name)
            );
            Item item = Registry.register(
                    BuiltInRegistries.ITEM,
                    itemKey,
                    new Item(
                            new Item.Properties()
                                    .humanoidArmor(ModArmorMaterials.CAT_EARS, ArmorType.HELMET)
                                    .durability(ArmorType.HELMET.getDurability(ModArmorMaterials.BASE_DURABILITY))
                                    .setId(itemKey)
                    )
            );
            CAT_EARS.put(color, item);
        }
    }
}
