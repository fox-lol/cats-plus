package xyz.foxkin.catsplus.client.model.entity.player;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import xyz.foxkin.catsplus.client.access.render.AnimatableContainer;
import xyz.foxkin.catsplus.client.animatable.player.FirstPersonPlayerArms;
import xyz.foxkin.catsplus.client.animatable.player.ThirdPersonPlayerArms;
import xyz.foxkin.catsplus.client.init.ModGeoRenderers;
import xyz.foxkin.catsplus.client.model.entity.CatsPlusModel;

@Environment(EnvType.CLIENT)
public class FirstPersonPlayerArmsModel extends PlayerArmsModel<FirstPersonPlayerArms> {

    public FirstPersonPlayerArmsModel() {
        super("geo/entity/player/entity_holding_arms_first_person.geo.json", "animations/entity/player/entity_holding_arms_first_person.animation.json");
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void tickOtherPerspectiveAnimations(FirstPersonPlayerArms currentPerspective) {
        PlayerEntity player = currentPerspective.getEntity();
        AnimatableContainer<ThirdPersonPlayerArms> animatableContainer = (AnimatableContainer<ThirdPersonPlayerArms>) player;
        ThirdPersonPlayerArms animatable = animatableContainer.catsPlus$getAnimatable();
        CatsPlusModel<ThirdPersonPlayerArms> model = ModGeoRenderers.getModel(ThirdPersonPlayerArms.class).orElseThrow();
        model.setLivingAnimations(animatable);
    }
}
