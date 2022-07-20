package xyz.foxkin.catsplus.commonside.init;

import dev.architectury.registry.ReloadListenerRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resource.ResourceType;
import xyz.foxkin.catsplus.client.render.matrixscript.MatrixScriptManager;

public class ModResourceReloaders {

    @Environment(EnvType.CLIENT)
    public static void registerClientReloaders() {
        ReloadListenerRegistry.register(ResourceType.CLIENT_RESOURCES, MatrixScriptManager.INSTANCE);
    }
}
