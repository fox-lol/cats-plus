package xyz.foxkin.catsplus.client.animatable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import xyz.foxkin.catsplus.commonside.access.entitypickup.EntityAccess;

@Environment(EnvType.CLIENT)
public abstract class HoldableEntityAnimatable<U extends Entity> extends EntityAnimatable<U> {

    public HoldableEntityAnimatable(U entity) {
        super(entity);
    }

    @Override
    protected <T extends IAnimatable> PlayState animationPredicate(AnimationEvent<T> event) {
        if (event.getController().getAnimationState() != AnimationState.Running) {
            EntityAccess entityAccess = (EntityAccess) getEntity();
            int heldPoseNumber = entityAccess.catsPlus$getHeldPoseNumber();
            if (heldPoseNumber > 0) {
                playAnimations(true, "held.idle." + heldPoseNumber);
            }
        }
        return PlayState.CONTINUE;
    }
}
