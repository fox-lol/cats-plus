package xyz.foxkin.catsplus.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import xyz.foxkin.catsplus.client.init.ModClientEventHandlers;
import xyz.foxkin.catsplus.client.init.ModClientNetworkReceivers;
import xyz.foxkin.catsplus.client.init.ModClientResourceReloaders;
import xyz.foxkin.catsplus.client.init.ModGeoRenderers;

@Environment(EnvType.CLIENT)
public class CatsPlusClient {

    public static void init() {
        ModClientResourceReloaders.registerClientReloaders();
        ModClientEventHandlers.registerEventHandlers();
        ModClientNetworkReceivers.registerReceivers();
        ModGeoRenderers.registerRenderers();
    }
}
