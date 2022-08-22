package xyz.foxkin.catsplus.client.render.entity.player;

import xyz.foxkin.catsplus.client.animatable.player.ThirdPersonPlayerArms;
import xyz.foxkin.catsplus.client.model.entity.player.PlayerArmsModel;

public class ThirdPersonPlayerArmsRenderer extends PlayerArmsRenderer<ThirdPersonPlayerArms> {

    public ThirdPersonPlayerArmsRenderer(PlayerArmsModel<ThirdPersonPlayerArms> modelProvider) {
        super(modelProvider, ThirdPersonPlayerArms.class);
    }
}
