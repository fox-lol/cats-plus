package xyz.foxkin.catsplus.client.render.entity.player;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import xyz.foxkin.catsplus.client.animatable.entity.player.ThirdPersonPlayerArms;
import xyz.foxkin.catsplus.client.model.entity.player.PlayerArmsModel;

@Environment(EnvType.CLIENT)
public class ThirdPersonPlayerArmsRenderer extends PlayerArmsRenderer<ThirdPersonPlayerArms> {

    public ThirdPersonPlayerArmsRenderer(PlayerArmsModel<ThirdPersonPlayerArms> modelProvider) {
        super(modelProvider, ThirdPersonPlayerArms.class);
    }
}
