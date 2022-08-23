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

    void catsPlus$setRandomHeldPoseNumber();

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
    @SuppressWarnings("unused")
    void catsPlus$dropHeldEntity(Vec3d pos);

    /**
     * Clears the player's held entity and spawns it in the world
     * with a velocity in the direction of the player's heading.
     *
     * @param speed The initial speed of the thrown entity.
     */
    @SuppressWarnings("unused")
    void catsPlus$throwHeldEntity(double speed);

    /**
     * Gets the held pose number. Different numbers mean the
     * entity will be seen held in a different pose.
     *
     * @return The held pose number.
     */
    int catsPlus$getHeldPoseNumber();

    /**
     * Sets the held pose number. Different numbers mean the
     * entity will be seen held in a different pose.
     *
     * @param heldPoseNumber The held pose number.
     */
    void catsPlus$setHeldPoseNumber(int heldPoseNumber);

    /**
     * Whether the player is interacting with it's held entity.
     *
     * @return Whether the player is interacting with it's held entity.
     */
    boolean catsPlus$isInteractingWithHeldEntity();

    /**
     * Sets whether the player is interacting with it's held entity.
     *
     * @param interacting Whether the player is interacting with it's held entity.
     */
    void catsPlus$setInteractingWithHeldEntity(boolean interacting);
}
