package xyz.foxkin.catsplus.commonside;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.foxkin.catsplus.commonside.config.CatsPlusConfig;
import xyz.foxkin.catsplus.commonside.init.ModBlocks;
import xyz.foxkin.catsplus.commonside.init.ModItems;
import xyz.foxkin.catsplus.commonside.init.ModSounds;

public class CatsPlus {

    public static final String MOD_ID = "catsplus";

    public static final Logger LOGGER = LoggerFactory.getLogger("Cats Plus");

    private static CatsPlusConfig config;

    public static void init() {
        registerConfig();
        ModSounds.registerSounds();
        ModBlocks.registerBlocks();
        ModItems.registerItems();
    }

    private static void registerConfig() {
        AutoConfig.register(CatsPlusConfig.class, JanksonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(CatsPlusConfig.class).getConfig();
    }

    public static CatsPlusConfig getConfig() {
        return config;
    }
}
