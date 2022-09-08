package xyz.foxkin.catsplus.client.render.entity.player;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import xyz.foxkin.catsplus.client.animatable.entity.player.FirstPersonPlayerArms;
import xyz.foxkin.catsplus.client.model.entity.player.PlayerArmsModel;

@Environment(EnvType.CLIENT)
public class FirstPersonPlayerArmsRenderer extends PlayerArmsRenderer<FirstPersonPlayerArms> {

    public FirstPersonPlayerArmsRenderer(PlayerArmsModel<FirstPersonPlayerArms> modelProvider) {
        super(modelProvider, FirstPersonPlayerArms.class);
    }
}
