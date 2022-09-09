package xyz.foxkin.catsplus.client.model.entity.player;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import xyz.foxkin.catsplus.client.animatable.entity.player.FirstPersonPlayerArms;
import xyz.foxkin.catsplus.client.animatable.entity.player.ThirdPersonPlayerArms;
import xyz.foxkin.catsplus.client.init.ModGeoRenderers;
import xyz.foxkin.catsplus.client.model.CatsPlusModel;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class ThirdPersonPlayerArmsModel extends PlayerArmsModel<ThirdPersonPlayerArms> {

    public ThirdPersonPlayerArmsModel() {
        super("geo/entity/player/entity_holding_arms_third_person.geo.json", "animations/entity/player/entity_holding_arms_third_person.animation.json");
    }

    @Override
    protected void tickOtherPerspectiveAnimations(ThirdPersonPlayerArms currentPerspective) {
        if (Objects.equals(currentPerspective.getEntity(), MinecraftClient.getInstance().player)) {
            FirstPersonPlayerArms otherPerspective = FirstPersonPlayerArms.getInstance();
            CatsPlusModel<FirstPersonPlayerArms> otherPerspectiveModel = ModGeoRenderers.getModel(FirstPersonPlayerArms.class).orElseThrow();
            // This initializes the model which prevents the animation from replaying when the player switches perspectives for the first time.
            otherPerspectiveModel.getModel(otherPerspective);
            otherPerspectiveModel.setLivingAnimations(otherPerspective);
        }
    }
}
