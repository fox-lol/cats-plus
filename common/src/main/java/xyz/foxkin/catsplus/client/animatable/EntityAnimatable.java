package xyz.foxkin.catsplus.client.animatable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

@Environment(EnvType.CLIENT)
public abstract class EntityAnimatable<T extends Entity> extends CatsPlusAnimatable {

    private final T entity;

    public EntityAnimatable(T entity) {
        this.entity = entity;
    }

    @Override
    public int getUniqueId() {
        if (entity == null) {
            return 0;
        } else {
            return entity.getUuid().hashCode();
        }
    }

    /**
     * Whether the entity is a baby or not.
     *
     * @return Whether the entity is a baby or not.
     */
    public boolean isBaby() {
        if (entity instanceof LivingEntity livingEntity) {
            return livingEntity.isBaby();
        } else {
            return false;
        }
    }

    /**
     * Gets the entity this animatable is representing.
     *
     * @return The entity this animatable is representing.
     */
    protected T getEntity() {
        return entity;
    }
}
