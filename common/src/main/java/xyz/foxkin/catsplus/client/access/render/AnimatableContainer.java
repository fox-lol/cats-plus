package xyz.foxkin.catsplus.client.access.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import xyz.foxkin.catsplus.client.animatable.CatsPlusAnimatable;

@Environment(EnvType.CLIENT)
public interface AnimatableContainer<T extends CatsPlusAnimatable> {

    T catsPlus$getIAnimatable();
}
