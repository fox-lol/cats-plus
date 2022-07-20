package xyz.foxkin.catsplus.commonside.access.entitypickup;

import net.minecraft.entity.Entity;

public interface EntityContainer<T extends Entity> {

    void setEntity(T entity);
}
