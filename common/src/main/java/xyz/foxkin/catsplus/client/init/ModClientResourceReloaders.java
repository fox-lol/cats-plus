package xyz.foxkin.catsplus.client.init;

import dev.architectury.registry.ReloadListenerRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resource.ResourceType;
import xyz.foxkin.catsplus.client.matrixscript.MatrixScriptManager;

@Environment(EnvType.CLIENT)
public class ModClientResourceReloaders {

    public static void registerClientReloaders() {
        ReloadListenerRegistry.register(ResourceType.CLIENT_RESOURCES, MatrixScriptManager.INSTANCE);
    }
}
