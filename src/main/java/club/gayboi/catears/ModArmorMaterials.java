package club.gayboi.catears;

import java.util.EnumMap;
import java.util.List;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class ModArmorMaterials {
    public static final Holder<ArmorMaterial> CAT_EARS = Registry.registerForHolder(
            BuiltInRegistries.ARMOR_MATERIAL,
            ResourceLocation.fromNamespaceAndPath(CatEarsMod.MOD_ID, "cat_ears"),
            new ArmorMaterial(
                    // defense per slot :3
                    Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                        map.put(ArmorItem.Type.HELMET, 1);
                        map.put(ArmorItem.Type.CHESTPLATE, 0);
                        map.put(ArmorItem.Type.LEGGINGS, 0);
                        map.put(ArmorItem.Type.BOOTS, 0);
                        map.put(ArmorItem.Type.BODY, 0);
                    }),
                    15, // enchantment value :3
                    BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.CAT_AMBIENT), // equip sound :3
                    () -> Ingredient.of(Items.STRING), // repair ingredient :3
                    // armor texture layers :3
                    List.of(new ArmorMaterial.Layer(
                            ResourceLocation.fromNamespaceAndPath(CatEarsMod.MOD_ID, "cat_ears")
                    )),
                    0.0F, // toughness :3
                    0.0F // knockback resistance :3
            )
    );

    public static void register() {
        // registered inline via registerForHolder :3
    }
}
