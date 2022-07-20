package xyz.foxkin.catsplus.commonside.init;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class ModNetworkReceivers {

    /**
     * Registers the logical server receivers.
     */
    public static void registerServerReceivers() {
    }

    @Environment(EnvType.CLIENT)
    public static void registerClientReceivers() {
    }
}
