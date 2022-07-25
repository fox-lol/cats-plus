package xyz.foxkin.catsplus.commonside.access.entitypickup;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface PlayerEntityAccess {

    Optional<Entity> catsPlus$getHeldEntity();

    void catsPlus$setHeldEntity(@Nullable Entity entity);

    void catsPlus$clearHeldEntity();

    /**
     * Clears the player's held entity and spawns it in the world.
     *
     * @param x The initial x coordinate of the spawned entity.
     * @param y The initial y coordinate of the spawned entity.
     * @param z The initial z coordinate of the spawned entity.
     */
    void catsPlus$dropHeldEntity(double x, double y, double z);

    void catsPlus$dropHeldEntity(Vec3d pos);

    /**
     * Clears the player's held entity and spawns it in the world
     * with a velocity in the direction of the player's heading.
     *
     * @param speed The initial speed of the thrown entity.
     */
    void catsPlus$throwHeldEntity(double speed);
}
