package xyz.foxkin.catsplus.client.render.entity.player;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import xyz.foxkin.catsplus.client.animatable.entity.player.PlayerArms;
import xyz.foxkin.catsplus.client.model.entity.player.PlayerArmsModel;
import xyz.foxkin.catsplus.client.render.CatsPlusGeoRenderer;

@Environment(EnvType.CLIENT)
public abstract class PlayerArmsRenderer<T extends PlayerArms> extends CatsPlusGeoRenderer<T, PlayerArmsModel<T>> {

    public PlayerArmsRenderer(PlayerArmsModel<T> modelProvider, Class<T> animatableClass) {
        super(modelProvider, animatableClass);
    }
}
