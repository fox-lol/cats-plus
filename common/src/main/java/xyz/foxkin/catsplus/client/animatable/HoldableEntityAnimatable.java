package xyz.foxkin.catsplus.client.animatable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import xyz.foxkin.catsplus.commonside.access.entitypickup.EntityAccess;
import xyz.foxkin.catsplus.commonside.access.entitypickup.PlayerEntityAccess;

@Environment(EnvType.CLIENT)
public abstract class HoldableEntityAnimatable<U extends Entity> extends EntityAnimatable<U> {

    public HoldableEntityAnimatable(U entity) {
        super(entity);
    }

    @Override
    protected <T extends CatsPlusAnimatable> PlayState animationPredicate(AnimationEvent<T> event) {
        PlayState playState = super.animationPredicate(event);
        if (playState == PlayState.STOP) {
            return playState;
        } else {
            EntityAccess entityAccess = (EntityAccess) getEntity();
            PlayerEntity holder = entityAccess.catsPlus$getHolder();
            PlayerEntityAccess holderAccess = (PlayerEntityAccess) holder;
            int heldPoseNumber = holderAccess.catsPlus$getHeldPoseNumber();
            if (heldPoseNumber > 0) {
                AnimationController<?> controller = event.getController();
                if (controller.getAnimationState() == AnimationState.Stopped) {
                    playAnimations(DEFAULT_ANIMATION_TRANSITION_LENGTH_TICKS, true, "held.idle." + heldPoseNumber);
                } else {
                    int transitionLengthTicks = 10;
                    if (holderAccess.catsPlus$isInteractingWithHeldEntity()) {
                        playAnimations(transitionLengthTicks, true, "held.interacting." + heldPoseNumber);
                    } else {
                        playAnimations(transitionLengthTicks, true, "held.idle." + heldPoseNumber);
                    }
                }
            }
            return PlayState.CONTINUE;
        }
    }
}
