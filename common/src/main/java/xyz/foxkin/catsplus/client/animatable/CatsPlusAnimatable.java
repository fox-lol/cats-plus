package xyz.foxkin.catsplus.client.animatable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

@Environment(EnvType.CLIENT)
public abstract class CatsPlusAnimatable implements IAnimatable {

    private static final String ANIMATION_CONTROLLER_NAME = "controller";
    private static final int ANIMATION_TRANSITION_LENGTH_TICKS = 0;
    private final AnimationFactory factory = new AnimationFactory(this);
    private String[] pendingAnimationNames = new String[0];
    private boolean lastPendingAnimationShouldLoop = false;

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, ANIMATION_CONTROLLER_NAME, ANIMATION_TRANSITION_LENGTH_TICKS, this::animationPredicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @SuppressWarnings("SameReturnValue")
    protected <T extends IAnimatable> PlayState animationPredicate(AnimationEvent<T> event) {
        if (pendingAnimationNames.length > 0) {
            playAnimations(lastPendingAnimationShouldLoop, pendingAnimationNames);
            pendingAnimationNames = new String[0];
        }
        return PlayState.CONTINUE;
    }

    public void playAnimations(boolean lastShouldLoop, String... animationNames) {
        AnimationController<?> controller = GeckoLibUtil.getControllerForID(getFactory(), getUniqueId(), ANIMATION_CONTROLLER_NAME);
        AnimationBuilder builder = new AnimationBuilder();
        for (int i = 0; i < animationNames.length; i++) {
            String animationName = animationNames[i];
            if (i == animationNames.length - 1) {
                builder.addAnimation(animationName, lastShouldLoop);
            } else {
                builder.addAnimation(animationName);
            }
        }
        controller.setAnimation(builder);
    }

    public abstract int getUniqueId();

    public abstract Identifier getTexture();

    public void setPendingAnimations(String... pendingAnimationNames) {
        this.pendingAnimationNames = pendingAnimationNames;
    }

    public void setLastPendingAnimationShouldLoop(boolean lastShouldLoop) {
        this.lastPendingAnimationShouldLoop = lastShouldLoop;
    }
}
