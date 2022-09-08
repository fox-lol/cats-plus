package xyz.foxkin.catsplus.client.animatable.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import xyz.foxkin.catsplus.client.animatable.CatsPlusAnimatable;

@Environment(EnvType.CLIENT)
public abstract class EntityAnimatable<T extends Entity> extends CatsPlusAnimatable {

    private final T entity;

    public EntityAnimatable(T entity) {
        this.entity = entity;
    }

    @Override
    public int getUniqueId() {
        return entity.getUuid().hashCode();
    }

    /**
     * Gets the entity this animatable is representing.
     *
     * @return The entity this animatable is representing.
     */
    public T getEntity() {
        return entity;
    }
}
