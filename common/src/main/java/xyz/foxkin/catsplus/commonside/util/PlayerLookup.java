package xyz.foxkin.catsplus.commonside.util;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Collection;

public class PlayerLookup {

    @SuppressWarnings("unused")
    @ExpectPlatform
    public static Collection<ServerPlayerEntity> tracking(Entity entity) {
        throw new AssertionError();
    }
}
