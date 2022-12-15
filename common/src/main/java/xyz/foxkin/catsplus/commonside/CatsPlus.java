package xyz.foxkin.catsplus.commonside;

import com.mojang.logging.LogUtils;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import org.slf4j.Logger;
import xyz.foxkin.catsplus.commonside.config.CatsPlusConfig;
import xyz.foxkin.catsplus.commonside.init.*;

public class CatsPlus {

    public static final String MOD_ID = "catsplus";
    public static final Logger LOGGER = LogUtils.getLogger();

    private static CatsPlusConfig config;

    public static void init() {
        registerConfig();
        ModSounds.registerSounds();
        ModBlocks.registerBlocks();
        ModItems.registerItems();
        ModEventHandlers.registerEventHandlers();
        ModNetworkReceivers.registerReceivers();
        ModResourceReloaders.registerResourceReloaders();
    }

    private static void registerConfig() {
        AutoConfig.register(CatsPlusConfig.class, JanksonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(CatsPlusConfig.class).getConfig();
    }

    public static CatsPlusConfig getConfig() {
        return config;
    }
}
