package xyz.foxkin.catsplus.client.animatable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;

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

    public T getEntity() {
        return entity;
    }
}
