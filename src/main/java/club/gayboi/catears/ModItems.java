package club.gayboi.catears;

import java.util.EnumMap;
import java.util.Map;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CatEarsMod.MOD_ID);

    // all 16 color variants :3
    public static final Map<DyeColor, DeferredItem<? extends Item>> CAT_EARS = new EnumMap<>(DyeColor.class);

    static {
        for (DyeColor color : DyeColor.values()) {
            String name = color.getName() + "_cat_ears";
            CAT_EARS.put(color, ITEMS.register(name, () -> new ArmorItem(
                    ModArmorMaterials.CAT_EARS,
                    ArmorItem.Type.HELMET,
                    new Item.Properties().durability(55)
            )));
        }
    }
}
