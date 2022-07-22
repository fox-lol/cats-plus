package xyz.foxkin.catsplus.mixin.forge.commonside.accessor.playertracking;

import net.minecraft.server.world.EntityTrackingListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(targets = "net/minecraft/server/world/ThreadedAnvilChunkStorage$EntityTracker")
public interface EntityTrackerAccessor {

    @Accessor("listeners")
    Set<EntityTrackingListener> catsPlus$getListeners();
}
