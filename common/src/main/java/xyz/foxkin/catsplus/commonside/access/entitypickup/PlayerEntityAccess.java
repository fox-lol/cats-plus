package xyz.foxkin.catsplus.commonside.access.entitypickup;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface PlayerEntityAccess {

    /**
     * Gets an {@code Optional} representing the held entity.
     * If there is no held entity, the {@code Optional} will be empty.
     *
     * @return An {@code Optional} representing the held entity.
     */
    Optional<Entity> catsPlus$getHeldEntity();

    /**
     * Sets the held entity.
     *
     * @param entity The entity to set.
     */
    void catsPlus$setHeldEntity(@Nullable Entity entity);

    /**
     * Clears the held entity.
     */
    void catsPlus$clearHeldEntity();

    /**
     * Clears the player's held entity and spawns it in the world.
     *
     * @param x The initial x coordinate of the spawned entity.
     * @param y The initial y coordinate of the spawned entity.
     * @param z The initial z coordinate of the spawned entity.
     */
    void catsPlus$dropHeldEntity(double x, double y, double z);

    /**
     * Drops the held entity in the world.
     *
     * @param pos The position to drop the entity at.
     */
    void catsPlus$dropHeldEntity(Vec3d pos);

    /**
     * Clears the player's held entity and spawns it in the world
     * with a velocity in the direction of the player's heading.
     *
     * @param speed The initial speed of the thrown entity.
     */
    void catsPlus$throwHeldEntity(double speed);
}
