package xyz.foxkin.catsplus.client.animatable.player;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.Animation;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import xyz.foxkin.catsplus.client.animatable.CatsPlusAnimatable;
import xyz.foxkin.catsplus.client.animatable.EntityAnimatable;
import xyz.foxkin.catsplus.commonside.access.entitypickup.PlayerEntityAccess;

@Environment(EnvType.CLIENT)
public abstract class PlayerArms extends EntityAnimatable<AbstractClientPlayerEntity> {

    private final boolean firstPerson;

    public PlayerArms(AbstractClientPlayerEntity player, boolean firstPerson) {
        super(player);
        this.firstPerson = firstPerson;
    }

    @Override
    protected <T extends CatsPlusAnimatable> PlayState animationPredicate(AnimationEvent<T> event) {
        PlayState playState = super.animationPredicate(event);
        if (playState == PlayState.STOP) {
            return playState;
        } else {
            AnimationController<?> controller = event.getController();
            Animation currentAnimation = controller.getCurrentAnimation();
            String currentAnimationName = currentAnimation == null ? "" : currentAnimation.animationName;
            PlayerEntityAccess playerAccess = (PlayerEntityAccess) getEntity();
            playerAccess.catsPlus$getHeldEntity().ifPresent(heldEntity -> {
                int heldPoseNumber = playerAccess.catsPlus$getHeldPoseNumber();
                if (heldPoseNumber > 0) {
                    Identifier entityId = EntityType.getId(heldEntity.getType());
                    boolean isBaby = heldEntity instanceof LivingEntity livingEntity && livingEntity.isBaby();
                    int transitionLengthTicks = 10;
                    if (playerAccess.catsPlus$isInteractingWithHeldEntity() && (currentAnimationName.contains("idle") || currentAnimationName.contains("interacting"))) {
                        playAnimations(transitionLengthTicks, true,
                                getAnimationPrefix()
                                        + "holding."
                                        + (isBaby ? "baby." : "")
                                        + entityId.getNamespace()
                                        + "_"
                                        + entityId.getPath()
                                        + "."
                                        + "interacting"
                                        + "."
                                        + heldPoseNumber
                        );
                    } else if (controller.getAnimationState() == AnimationState.Stopped || currentAnimationName.contains("interacting")) {
                        playAnimations(transitionLengthTicks, true,
                                getAnimationPrefix()
                                        + "holding."
                                        + (isBaby ? "baby." : "")
                                        + entityId.getNamespace()
                                        + "_"
                                        + entityId.getPath()
                                        + "."
                                        + "idle"
                                        + "."
                                        + heldPoseNumber
                        );
                    }
                }
            });
            return PlayState.CONTINUE;
        }
    }

    @Override
    public Identifier getTexture() {
        return getEntity().getSkinTexture();
    }

    /**
     * Gets the animation name prefix corresponding to the arm perspective.
     *
     * @return The animation name prefix corresponding to the arm perspective.
     */
    private String getAnimationPrefix() {
        return (firstPerson ? "first" : "third") + "_person.";
    }
}
