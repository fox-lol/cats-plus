package xyz.foxkin.catsplus.commonside.util;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Collection;

public class PlayerLookup {

    /**
     * Gets all players tracking an entity in a server world.
     *
     * <p>The returned collection is immutable.
     *
     * <p><b>Warning</b>: If the provided entity is a player, it is not
     * guaranteed by the contract that said player is included in the
     * resulting stream.
     *
     * @param entity the entity being tracked
     * @return the players tracking the entity
     * @throws IllegalArgumentException if the entity is not in a server world
     */
    @SuppressWarnings("unused")
    @ExpectPlatform
    public static Collection<ServerPlayerEntity> tracking(Entity entity) {
        throw new AssertionError();
    }
}
