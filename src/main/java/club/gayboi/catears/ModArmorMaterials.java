package club.gayboi.catears;

import java.util.Map;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.animal.feline.CatSoundVariants;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

public final class ModArmorMaterials {
    public static final int DURABILITY_MULTIPLIER = 55;

    public static final TagKey<Item> REPAIRS_CAT_EARS = TagKey.create(
            BuiltInRegistries.ITEM.key(),
            Identifier.fromNamespaceAndPath(CatEarsMod.MOD_ID, "repairs_cat_ears")
    );

    public static final ResourceKey<EquipmentAsset> CAT_EARS_EQUIPMENT_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(CatEarsMod.MOD_ID, "cat_ears")
    );

    public static final ArmorMaterial CAT_EARS = new ArmorMaterial(
            DURABILITY_MULTIPLIER,
            Map.of(ArmorType.HELMET, 1),
            15,
            SoundEvents.CAT_SOUNDS.get(CatSoundVariants.SoundSet.CLASSIC).adultSounds().ambientSound(),
            0.0F,
            0.0F,
            REPAIRS_CAT_EARS,
            CAT_EARS_EQUIPMENT_ASSET
    );

    private ModArmorMaterials() {}
}
