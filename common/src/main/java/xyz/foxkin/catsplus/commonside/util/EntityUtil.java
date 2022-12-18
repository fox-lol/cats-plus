package xyz.foxkin.catsplus.commonside.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.Optional;

public class EntityUtil {

    /**
     * Writes information about an entity to NBT.
     *
     * @param entity The entity to write to NBT.
     * @return The NBT representation of the entity.
     */
    public static NbtCompound writeEntity(Entity entity) {
        NbtCompound entityNbt = new NbtCompound();
        Identifier entityId = EntityType.getId(entity.getType());
        entityNbt.putString("id", entityId.toString());
        entity.writeNbt(entityNbt);
        return entityNbt;
    }

    public static Optional<Entity> readEntity(NbtCompound entityNbt, World world) {
        if (entityNbt.isEmpty()) {
            return Optional.empty();
        } else {
            return EntityType.getEntityFromNbt(entityNbt, world);
        }
    }

    /**
     * Copies an entity.
     *
     * @param entity The entity to copy.
     * @param world  The world the copied entity will be in.
     * @return The copied entity.
     */
    public static Entity copyEntity(Entity entity, World world) {
        NbtCompound entityNbt = writeEntity(entity);
        return readEntity(entityNbt, world).orElseThrow(
                () -> new IllegalArgumentException("Entity with type " + entity.getType() + " produced invalid NBT:\n" + entityNbt)
        );
    }
}
