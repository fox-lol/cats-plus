package xyz.foxkin.catsplus.client.render.entity.player;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import xyz.foxkin.catsplus.client.animatable.player.PlayerArms;
import xyz.foxkin.catsplus.client.model.entity.CatsPlusModel;
import xyz.foxkin.catsplus.client.render.CatsPlusGeoRenderer;

@Environment(EnvType.CLIENT)
public class PlayerArmsRenderer extends CatsPlusGeoRenderer<PlayerArms> {

    public PlayerArmsRenderer(CatsPlusModel<PlayerArms> modelProvider) {
        super(modelProvider, PlayerArms.class);
    }
}
