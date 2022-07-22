package xyz.foxkin.catsplus.commonside;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.foxkin.catsplus.client.init.ModClientEventHandlers;
import xyz.foxkin.catsplus.client.init.ModClientNetworkReceivers;
import xyz.foxkin.catsplus.client.init.ModClientResourceReloaders;
import xyz.foxkin.catsplus.client.init.ModGeoRenderers;
import xyz.foxkin.catsplus.commonside.config.CatsPlusConfig;
import xyz.foxkin.catsplus.commonside.init.*;

public class CatsPlus {

    public static final String MOD_ID = "catsplus";
    public static final Logger LOGGER = LoggerFactory.getLogger("Cats Plus");

    private static CatsPlusConfig config;

    public static void init() {
        registerConfig();
        ModSounds.registerSounds();
        ModBlocks.registerBlocks();
        ModItems.registerItems();
        ModEventHandlers.registerEventHandlers();
        ModNetworkReceivers.registerReceivers();
        ModEntityHeldPoses.addEntityHeldPosesCounts();
    }

    @Environment(EnvType.CLIENT)
    public static void clientInit() {
        ModClientResourceReloaders.registerClientReloaders();
        ModClientEventHandlers.registerEventHandlers();
        ModClientNetworkReceivers.registerReceivers();
        ModGeoRenderers.registerRenderers();
    }

    private static void registerConfig() {
        AutoConfig.register(CatsPlusConfig.class, JanksonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(CatsPlusConfig.class).getConfig();
    }

    public static CatsPlusConfig getConfig() {
        return config;
    }
}
