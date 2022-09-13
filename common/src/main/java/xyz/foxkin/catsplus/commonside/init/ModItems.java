package xyz.foxkin.catsplus.commonside.init;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import xyz.foxkin.catsplus.commonside.CatsPlus;

@SuppressWarnings("unused")
public class ModItems {

    private static final ItemGroup CATS_PLUS_GROUP = CreativeTabRegistry.create(
            new Identifier(CatsPlus.MOD_ID, "items"),
            () -> new ItemStack(ModItems.CAT_BLOCK.get())
    );

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(CatsPlus.MOD_ID, Registry.ITEM_KEY);

    public static final RegistrySupplier<Item> CAT_MAID_SCRAP = ITEMS.register(
            "cat_maid_scrap",
            () -> new Item(new Item.Settings().group(CATS_PLUS_GROUP))
    );

    public static final RegistrySupplier<Item> CAT_MAID_HELMET = ITEMS.register(
            "cat_maid_helmet",
            () -> new ArmorItem(
                    ModArmorMaterials.CAT_MAID_ARMOR,
                    EquipmentSlot.HEAD,
                    new Item.Settings().group(CATS_PLUS_GROUP).maxCount(1)
            )
    );

    public static final RegistrySupplier<Item> CAT_MAID_CHESTPLATE = ITEMS.register(
            "cat_maid_chestplate",
            () -> new ArmorItem(
                    ModArmorMaterials.CAT_MAID_ARMOR,
                    EquipmentSlot.CHEST,
                    new Item.Settings().group(CATS_PLUS_GROUP).maxCount(1)
            )
    );

    public static final RegistrySupplier<Item> CAT_MAID_LEGGINGS = ITEMS.register(
            "cat_maid_leggings",
            () -> new ArmorItem(
                    ModArmorMaterials.CAT_MAID_ARMOR,
                    EquipmentSlot.LEGS,
                    new Item.Settings().group(CATS_PLUS_GROUP).maxCount(1)
            )
    );

    public static final RegistrySupplier<Item> CAT_MAID_BOOTS = ITEMS.register(
            "cat_maid_boots",
            () -> new ArmorItem(
                    ModArmorMaterials.CAT_MAID_ARMOR,
                    EquipmentSlot.FEET,
                    new Item.Settings().group(CATS_PLUS_GROUP).maxCount(1)
            )
    );

    // Block items

    public static final RegistrySupplier<Item> CAT_BLOCK = ITEMS.register(
            "cat_block",
            () -> new BlockItem(ModBlocks.CAT_BLOCK.get(), new Item.Settings().group(CATS_PLUS_GROUP))
    );


    public static void registerItems() {
        ITEMS.register();
    }
}
