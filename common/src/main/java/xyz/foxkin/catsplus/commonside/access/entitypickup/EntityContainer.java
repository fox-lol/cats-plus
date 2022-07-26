package xyz.foxkin.catsplus.commonside.access.entitypickup;

import net.minecraft.entity.Entity;

public interface EntityContainer<T extends Entity> {

    /**
     * Sets the entity.
     *
     * @param entity The entity to set.
     */
    void setEntity(T entity);
}
