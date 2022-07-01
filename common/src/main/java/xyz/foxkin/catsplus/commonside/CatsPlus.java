package xyz.foxkin.catsplus.commonside;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.foxkin.catsplus.commonside.config.CatsPlusConfig;
import xyz.foxkin.catsplus.commonside.registry.ModBlocks;
import xyz.foxkin.catsplus.commonside.registry.ModItems;
import xyz.foxkin.catsplus.commonside.registry.ModSounds;

public class CatsPlus {

    private static CatsPlusConfig config;

    public static final String MOD_ID = "catsplus";

    public static final Logger LOGGER = LoggerFactory.getLogger("Cats Plus");

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
