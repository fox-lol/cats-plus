package xyz.foxkin.catsplus.client.model.entity.player;

import net.minecraft.entity.player.PlayerEntity;
import xyz.foxkin.catsplus.client.access.render.AnimatableContainer;
import xyz.foxkin.catsplus.client.animatable.player.FirstPersonPlayerArms;
import xyz.foxkin.catsplus.client.animatable.player.ThirdPersonPlayerArms;
import xyz.foxkin.catsplus.client.init.ModGeoRenderers;
import xyz.foxkin.catsplus.client.model.entity.CatsPlusModel;

public class FirstPersonPlayerArmsModel extends PlayerArmsModel<FirstPersonPlayerArms> {

    @SuppressWarnings("unchecked")
    @Override
    protected void tickOtherPerspectiveAnimations(FirstPersonPlayerArms currentPerspective) {
        var renderer = ModGeoRenderers.getRenderer(ThirdPersonPlayerArms.class).orElseThrow();
        PlayerEntity player = currentPerspective.getEntity();
        AnimatableContainer<ThirdPersonPlayerArms> animatableContainer = (AnimatableContainer<ThirdPersonPlayerArms>) player;
        ThirdPersonPlayerArms animatable = animatableContainer.catsPlus$getAnimatable();
        CatsPlusModel<ThirdPersonPlayerArms> model = renderer.getGeoModelProvider();
        model.setLivingAnimations(animatable, animatable.getUniqueId());
    }
}
