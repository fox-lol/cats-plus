package xyz.foxkin.catsplus.commonside.util.forge;

import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.EntityTrackingListener;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.world.chunk.ChunkManager;
import xyz.foxkin.catsplus.mixin.forge.commonside.accessor.playertracking.EntityTrackerAccessor;
import xyz.foxkin.catsplus.mixin.forge.commonside.accessor.playertracking.ThreadedAnvilChunkStorageAccessor;

import java.util.Collection;
import java.util.Objects;

public class PlayerLookupImpl {

    public static Collection<ServerPlayerEntity> tracking(Entity entity) {
        Objects.requireNonNull(entity, "Entity cannot be null");
        ChunkManager manager = entity.getWorld().getChunkManager();
        if (manager instanceof ServerChunkManager) {
            ThreadedAnvilChunkStorage storage = ((ServerChunkManager) manager).threadedAnvilChunkStorage;
            EntityTrackerAccessor tracker = ((ThreadedAnvilChunkStorageAccessor) storage).catsPlus$getEntityTrackers().get(entity.getId());
            if (tracker != null) {
                return tracker.catsPlus$getListeners().stream().map(EntityTrackingListener::getPlayer).collect(ImmutableSet.toImmutableSet());
            } else {
                return ImmutableSet.of();
            }
        } else {
            throw new IllegalArgumentException("Only supported on server worlds!");
        }
    }
}
