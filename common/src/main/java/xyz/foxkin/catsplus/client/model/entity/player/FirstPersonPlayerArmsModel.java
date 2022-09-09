package xyz.foxkin.catsplus.client.model.entity.player;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import xyz.foxkin.catsplus.client.access.render.AnimatableContainer;
import xyz.foxkin.catsplus.client.animatable.entity.player.FirstPersonPlayerArms;
import xyz.foxkin.catsplus.client.animatable.entity.player.ThirdPersonPlayerArms;
import xyz.foxkin.catsplus.client.init.ModGeoRenderers;
import xyz.foxkin.catsplus.client.model.CatsPlusModel;

@Environment(EnvType.CLIENT)
public class FirstPersonPlayerArmsModel extends PlayerArmsModel<FirstPersonPlayerArms> {

    public FirstPersonPlayerArmsModel() {
        super("geo/entity/player/entity_holding_arms_first_person.geo.json", "animations/entity/player/entity_holding_arms_first_person.animation.json");
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void tickOtherPerspectiveAnimations(FirstPersonPlayerArms currentPerspective) {
        PlayerEntity player = currentPerspective.getEntity();
        AnimatableContainer<ThirdPersonPlayerArms> container = (AnimatableContainer<ThirdPersonPlayerArms>) player;
        ThirdPersonPlayerArms otherPerspective = container.catsPlus$getAnimatable();
        CatsPlusModel<ThirdPersonPlayerArms> otherPerspectiveModel = ModGeoRenderers.getModel(ThirdPersonPlayerArms.class).orElseThrow();
        // This initializes the model which prevents the animation from replaying when the player switches perspectives for the first time.
        otherPerspectiveModel.getModel(otherPerspective);
        otherPerspectiveModel.setLivingAnimations(otherPerspective);
    }
}
