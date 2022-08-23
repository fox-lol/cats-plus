package xyz.foxkin.catsplus.commonside.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class EntityUtil {

    /**
     * Writes information about the player's held entity to NBT.
     *
     * @param entity The entity to serialize.
     * @return The NBT representation of the entity.
     */
    public static NbtCompound serializeEntity(Entity entity) {
        NbtCompound entityNbt = new NbtCompound();
        Identifier entityId = EntityType.getId(entity.getType());
        entityNbt.putString("id", entityId.toString());
        entity.writeNbt(entityNbt);
        return entityNbt;
    }

    /**
     * Copies an entity.
     *
     * @param entity The entity to copy.
     * @param world  The world the copied entity will be in.
     * @return The copied entity.
     */
    public static Entity copyEntity(Entity entity, World world) {
        NbtCompound entityNbt = serializeEntity(entity);
        return EntityType.getEntityFromNbt(entityNbt, world).orElseThrow(
                () -> new IllegalArgumentException("Entity with type " + entity.getType() + " produced invalid NBT:\n" + entityNbt)
        );
    }
}
