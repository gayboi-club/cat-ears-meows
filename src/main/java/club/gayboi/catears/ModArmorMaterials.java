package club.gayboi.catears;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModArmorMaterials {
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS =
            DeferredRegister.create(net.minecraft.core.registries.Registries.ARMOR_MATERIAL, CatEarsMod.MOD_ID);

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> CAT_EARS =
            ARMOR_MATERIALS.register("cat_ears", () -> new ArmorMaterial(
                    // defense per slot :3
                    Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                        map.put(ArmorItem.Type.HELMET, 1);
                        map.put(ArmorItem.Type.CHESTPLATE, 0);
                        map.put(ArmorItem.Type.LEGGINGS, 0);
                        map.put(ArmorItem.Type.BOOTS, 0);
                        map.put(ArmorItem.Type.BODY, 0);
                    }),
                    15, // enchantment value :3
                    net.minecraft.core.registries.BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.CAT_AMBIENT), // equip sound :3
                    () -> Ingredient.of(Items.STRING), // repair ingredient :3
                    // armor texture layers :3
                    List.of(new ArmorMaterial.Layer(
                            ResourceLocation.fromNamespaceAndPath(CatEarsMod.MOD_ID, "cat_ears")
                    )),
                    0.0F, // toughness :3
                    0.0F // knockback resistance :3
            ));
}
