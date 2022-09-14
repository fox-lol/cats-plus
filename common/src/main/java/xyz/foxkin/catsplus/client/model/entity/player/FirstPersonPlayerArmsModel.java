package xyz.foxkin.catsplus.client.model.entity.player;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.entity.player.PlayerEntity;
import software.bernie.geckolib3.core.processor.IBone;
import xyz.foxkin.catsplus.client.access.render.AnimatableContainer;
import xyz.foxkin.catsplus.client.animatable.entity.player.FirstPersonPlayerArms;
import xyz.foxkin.catsplus.client.animatable.entity.player.PlayerArms;
import xyz.foxkin.catsplus.client.animatable.entity.player.ThirdPersonPlayerArms;
import xyz.foxkin.catsplus.client.init.ModGeoRenderers;
import xyz.foxkin.catsplus.client.model.CatsPlusModel;

@Environment(EnvType.CLIENT)
public class FirstPersonPlayerArmsModel extends PlayerArmsModel<FirstPersonPlayerArms> {

    public FirstPersonPlayerArmsModel() {
        super("geo/entity/player/entity_holding_arms_first_person.geo.json", "animations/entity/player/entity_holding_arms_first_person.animation.json");
    }

    @Override
    public void setUp(FirstPersonPlayerArms animatable) {
        super.setUp(animatable);
        hideArmParts(animatable);
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

    /**
     * Hides arm parts under certain conditions.
     *
     * @param playerArms The animatable representing the player's arms.
     */
    private void hideArmParts(PlayerArms playerArms) {
        boolean slimArms = playerArms.getEntity().getModel().equals("slim");
        PlayerEntity player = playerArms.getEntity();
        boolean invisible = player.isInvisible();
        boolean invisibleOrSlim = invisible || slimArms;
        boolean invisibleOrNotSlim = invisible || !slimArms;
        boolean rightSleeveHidden = !MinecraftClient.getInstance().options.isPlayerModelPartEnabled(PlayerModelPart.RIGHT_SLEEVE);
        boolean leftSleeveHidden = !MinecraftClient.getInstance().options.isPlayerModelPartEnabled(PlayerModelPart.LEFT_SLEEVE);

        setBoneHidden("right_arm_wide", invisibleOrSlim);
        setBoneHidden("left_arm_wide", invisibleOrSlim);
        setBoneHidden("right_arm_layer_wide", invisibleOrSlim || rightSleeveHidden);
        setBoneHidden("left_arm_layer_wide", invisibleOrSlim || leftSleeveHidden);

        setBoneHidden("right_arm_slim", invisibleOrNotSlim);
        setBoneHidden("left_arm_slim", invisibleOrNotSlim);
        setBoneHidden("right_arm_layer_slim", invisibleOrNotSlim || rightSleeveHidden);
        setBoneHidden("left_arm_layer_slim", invisibleOrNotSlim || leftSleeveHidden);
    }

    private void setBoneHidden(String boneName, boolean hidden) {
        IBone bone = getAnimationProcessor().getBone(boneName);
        if (bone != null) {
            bone.setHidden(hidden);
        }
    }
}
