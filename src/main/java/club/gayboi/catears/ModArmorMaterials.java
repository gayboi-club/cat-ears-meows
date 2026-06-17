package club.gayboi.catears;

import java.util.Map;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

public class ModArmorMaterials {
    public static final int BASE_DURABILITY = 55;

    public static final TagKey<Item> REPAIRS_CAT_EARS = TagKey.create(
            BuiltInRegistries.ITEM.key(),
            Identifier.fromNamespaceAndPath(CatEarsMod.MOD_ID, "repairs_cat_ears")
    );

    public static final ResourceKey<EquipmentAsset> CAT_EARS_ARMOR_MATERIAL_KEY = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(CatEarsMod.MOD_ID, "cat_ears")
    );

    public static final ArmorMaterial CAT_EARS = new ArmorMaterial(
            BASE_DURABILITY,
            Map.of(ArmorType.HELMET, 1),
            15, // enchantment value :3
            BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.CAT_AMBIENT),
            0.0F, // toughness :3
            0.0F, // knockback resistance :3
            REPAIRS_CAT_EARS,
            CAT_EARS_ARMOR_MATERIAL_KEY
    );

    public static void register() {
        // material is used directly via instance, no registry needed :3
    }
}
