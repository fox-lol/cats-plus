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

@Environment(EnvType.CLIENT)
public abstract class CatsPlusAnimatable implements IAnimatable {

    private AnimationFactory factory = new AnimationFactory(this);
    private String[] pendingAnimationNames = new String[0];
    private int pendingAnimationTransitionLengthTicks = 0;
    private boolean lastPendingAnimationShouldLoop = false;

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 0, this::animationPredicate));
        animationData.addAnimationController(new AnimationController<>(this, "constant_animation_controller", 0, this::constantAnimationPredicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    /**
     * The animation predicate which is run every frame.
     *
     * @param event The animation event.
     * @param <T>   The animatable type.
     * @return The play state.
     */
    protected <T extends CatsPlusAnimatable> PlayState animationPredicate(AnimationEvent<T> event) {
        if (pendingAnimationNames.length > 0) {
            AnimationController<?> controller = event.getController();
            playAnimations(controller, pendingAnimationTransitionLengthTicks, lastPendingAnimationShouldLoop, pendingAnimationNames);
            clearPendingAnimations();
        }
        return PlayState.CONTINUE;
    }

    protected <T extends CatsPlusAnimatable> PlayState constantAnimationPredicate(AnimationEvent<T> event) {
        return PlayState.CONTINUE;
    }

    /**
     * Plays the given animations.
     *
     * @param controller            The animation controller.
     * @param transitionLengthTicks The length of the animation transition.
     * @param lastShouldLoop        Whether the last animation should loop.
     * @param animationNames        The animation names.
     */
    protected void playAnimations(AnimationController<?> controller, int transitionLengthTicks, boolean lastShouldLoop, String... animationNames) {
        AnimationBuilder builder = new AnimationBuilder();
        for (int i = 0; i < animationNames.length; i++) {
            String animationName = animationNames[i];
            if (i == animationNames.length - 1) {
                builder.addAnimation(animationName, lastShouldLoop);
            } else {
                builder.addAnimation(animationName);
            }
        }
        controller.transitionLengthTicks = transitionLengthTicks;
        controller.setAnimation(builder);
    }

    /**
     * Gets the unique ID for this animatable.
     *
     * @return The unique ID.
     */
    public abstract int getUniqueId();

    /**
     * Gets an {@code Identifier} pointing to the texture location for this animatable.
     *
     * @return The texture location for this animatable.
     */
    public abstract Identifier getTexture();

    /**
     * Sets animations to be played when {@link #animationPredicate(AnimationEvent)} is next run.
     *
     * @param transitionLengthTicks The length of the animation transition.
     * @param lastShouldLoop        Whether the last animation should loop.
     * @param animationNames        The animation names. If no animations are given,
     *                              the pending animations will be cleared.
     */
    public void setPendingAnimations(int transitionLengthTicks, boolean lastShouldLoop, String... animationNames) {
        pendingAnimationNames = animationNames;
        pendingAnimationTransitionLengthTicks = transitionLengthTicks;
        lastPendingAnimationShouldLoop = lastShouldLoop;
    }

    /**
     * Clears the pending animations.
     */
    public void clearPendingAnimations() {
        pendingAnimationNames = new String[0];
        pendingAnimationTransitionLengthTicks = 0;
        lastPendingAnimationShouldLoop = false;
    }

    /**
     * Cancels the current animation and clears the pending animations.
     */
    public void cancelAnimations() {
        clearPendingAnimations();
        factory = new AnimationFactory(this);
    }
}
