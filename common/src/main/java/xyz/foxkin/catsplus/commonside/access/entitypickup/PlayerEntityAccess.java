package xyz.foxkin.catsplus.commonside.access.entitypickup;

import net.minecraft.nbt.NbtCompound;

public interface PlayerEntityAccess {

    /**
     * Gets an {@code NbtCompound} containing information about the player's held entity.
     * If the player has no held entity, the {@code NbtCompound} will be empty.
     *
     * @return An {@code NbtCompound} containing information about the player's held entity.
     */
    NbtCompound catsPlus$getHeldEntity();

    /**
     * Gets whether the player has a held entity or not.
     *
     * @return Whether the player has a held entity or not.
     */

    boolean catsPlus$isHoldingEntity();

    /**
     * Sets the player's held entity.
     *
     * @param nbtCompound The {@code NbtCompound} containing information about the player's held entity.
     */

    void catsPlus$setHeldEntity(NbtCompound nbtCompound);

    /**
     * Clears the player's held entity.
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
     * Clears the player's held entity and spawns it in the world
     * with a velocity in the direction of the player's heading.
     *
     * @param speed The initial speed of the thrown entity.
     */
    void catsPlus$throwHeldEntity(double speed);
}
