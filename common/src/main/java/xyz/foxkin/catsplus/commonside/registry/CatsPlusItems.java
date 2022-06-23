package xyz.foxkin.catsplus.commonside.registry;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import xyz.foxkin.catsplus.commonside.CatsPlus;

@SuppressWarnings("unused")
public class CatsPlusItems {

    private static final ItemGroup CATS_PLUS_GROUP = CreativeTabRegistry.create(
            new Identifier(CatsPlus.MOD_ID, "items"),
            () -> new ItemStack(CatsPlusItems.CAT_BLOCK.get())
    );

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(CatsPlus.MOD_ID, Registry.ITEM_KEY);

    // Block items

    public static final RegistrySupplier<Item> CAT_BLOCK = ITEMS.register(
            "cat_block",
            () -> new BlockItem(CatsPlusBlocks.CAT_BLOCK.get(), new Item.Settings().group(CATS_PLUS_GROUP))
    );

    public static void registerItems() {
        ITEMS.register();
    }
}
