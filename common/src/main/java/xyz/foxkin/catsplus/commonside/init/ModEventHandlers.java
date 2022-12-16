package xyz.foxkin.catsplus.commonside.init;

import dev.architectury.event.events.common.LootEvent;
import xyz.foxkin.catsplus.commonside.event.LootTableEventHandler;

public class ModEventHandlers {

    public static void registerEventHandlers() {
        LootEvent.MODIFY_LOOT_TABLE.register(LootTableEventHandler.INSTANCE);
    }
}
