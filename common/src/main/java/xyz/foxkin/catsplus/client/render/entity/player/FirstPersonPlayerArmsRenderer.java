package xyz.foxkin.catsplus.client.render.entity.player;

import xyz.foxkin.catsplus.client.animatable.player.FirstPersonPlayerArms;
import xyz.foxkin.catsplus.client.model.entity.player.PlayerArmsModel;

public class FirstPersonPlayerArmsRenderer extends PlayerArmsRenderer<FirstPersonPlayerArms> {

    public FirstPersonPlayerArmsRenderer(PlayerArmsModel<FirstPersonPlayerArms> modelProvider) {
        super(modelProvider, FirstPersonPlayerArms.class);
    }
}
