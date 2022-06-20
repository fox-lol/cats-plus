package xyz.foxkin.catsplus.commonside;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CatsPlus {

    public static final String MOD_ID = "catsplus";

    // Registering a new creative tab
    public static final ItemGroup EXAMPLE_TAB = CreativeTabRegistry.create(
            new Identifier(MOD_ID, "example_tab"),
            () -> new ItemStack(CatsPlus.EXAMPLE_ITEM.get())
    );

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, Registry.ITEM_KEY);
    public static final RegistrySupplier<Item> EXAMPLE_ITEM = ITEMS.register(
            "example_item",
            () -> new Item(new Item.Settings().group(EXAMPLE_TAB))
    );
    
    public static void init() {
        ITEMS.register();
        System.out.println(ExampleExpectPlatform.getConfigDirectory().toAbsolutePath().normalize().toString());
    }
}
