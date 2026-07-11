package club.gayboi.catears;

import java.util.EnumMap;

import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class ModArmorMaterials {
    public static final int BASE_DURABILITY = 55;

    public static final ArmorMaterial CAT_EARS = new ArmorMaterial() {
        @Override
        public int getDurabilityForType(ArmorItem.Type type) {
            return BASE_DURABILITY;
        }

        @Override
        public int getDefenseForType(ArmorItem.Type type) {
            return type == ArmorItem.Type.HELMET ? 1 : 0;
        }

        @Override
        public int getEnchantmentValue() {
            return 15;
        }

        @Override
        public SoundEvent getEquipSound() {
            return SoundEvents.CAT_AMBIENT;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(Items.STRING);
        }

        @Override
        public String getName() {
            return "cat_ears";
        }

        @Override
        public float getToughness() {
            return 0.0F;
        }

        @Override
        public float getKnockbackResistance() {
            return 0.0F;
        }
    };

    public static void register() {
    }
}
