package xyz.foxkin.catsplus.commonside.init;

import dev.architectury.registry.ReloadListenerRegistry;
import net.minecraft.resource.ResourceType;
import xyz.foxkin.catsplus.commonside.animation.EntityHeldPosesManager;

public class ModResourceReloaders {

    public static void registerResourceReloaders() {
        ReloadListenerRegistry.register(ResourceType.SERVER_DATA, EntityHeldPosesManager.INSTANCE);
    }
}
