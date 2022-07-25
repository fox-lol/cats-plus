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
        if (entity == null) {
            return 0;
        } else {
            return entity.getUuid().hashCode();
        }
    }

    protected T getEntity() {
        return entity;
    }
}
