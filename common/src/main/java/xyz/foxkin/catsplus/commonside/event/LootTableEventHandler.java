package xyz.foxkin.catsplus.commonside.event;

import dev.architectury.event.events.common.LootEvent;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;
import xyz.foxkin.catsplus.commonside.init.ModItems;

public enum LootTableEventHandler implements LootEvent.ModifyLootTable {

    INSTANCE;

    @Override
    public void modifyLootTable(LootManager lootTables, Identifier id, LootEvent.LootTableModificationContext context, boolean builtin) {
        if (builtin && id.getPath().startsWith("chests/")) {
            LootPool.Builder poolBuilder = LootPool.builder()
                    .rolls(ConstantLootNumberProvider.create(1))
                    .conditionally(RandomChanceLootCondition.builder(0.05F))
                    .with(ItemEntry.builder(ModItems.SPRAY_BOTTLE.get()))
                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 1)));
            context.addPool(poolBuilder);
        }
    }
}
