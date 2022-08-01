package xyz.foxkin.catsplus.client.animatable.player;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import xyz.foxkin.catsplus.client.animatable.EntityAnimatable;
import xyz.foxkin.catsplus.commonside.access.entitypickup.EntityAccess;
import xyz.foxkin.catsplus.commonside.access.entitypickup.PlayerEntityAccess;

@Environment(EnvType.CLIENT)
public abstract class PlayerArms extends EntityAnimatable<AbstractClientPlayerEntity> {

    private final boolean firstPerson;

    public PlayerArms(AbstractClientPlayerEntity player, boolean firstPerson) {
        super(player);
        this.firstPerson = firstPerson;
    }

    @Override
    protected <T extends IAnimatable> PlayState animationPredicate(AnimationEvent<T> event) {
        super.animationPredicate(event);
        if (event.getController().getAnimationState() == AnimationState.Stopped) {
            PlayerEntityAccess playerAccess = (PlayerEntityAccess) getEntity();
            playerAccess.catsPlus$getHeldEntity().ifPresent(heldEntity -> {
                EntityAccess heldEntityAccess = (EntityAccess) heldEntity;
                int heldPoseNumber = heldEntityAccess.catsPlus$getHeldPoseNumber();
                if (heldPoseNumber > 0) {
                    Identifier entityId = EntityType.getId(heldEntity.getType());
                    boolean isBaby;
                    if (heldEntity instanceof LivingEntity livingEntity) {
                        isBaby = livingEntity.isBaby();
                    } else {
                        isBaby = false;
                    }
                    playAnimations(true,
                            getAnimationPrefix()
                                    + "holding."
                                    + (isBaby ? "baby." : "")
                                    + entityId.getNamespace()
                                    + "_" + entityId.getPath()
                                    + ".idle."
                                    + heldPoseNumber
                    );
                }
            });
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void playAnimations(boolean lastShouldLoop, String... animationNames) {
        super.playAnimations(lastShouldLoop, animationNames);
        clearOtherPerspectiveAnimations();
    }

    /**
     * Clears all animations from the other perspective when the
     * animations from this perspective have been played. This
     * is to prevent the animations from being played a second time
     * when the player switches perspectives.
     */
    protected abstract void clearOtherPerspectiveAnimations();

    @Override
    public Identifier getTexture() {
        return getEntity().getSkinTexture();
    }

    /**
     * Whether the player's skin has slim arms or not.
     *
     * @return Whether the player's skin has slim arms or not.
     */
    public boolean isSlimArms() {
        return getEntity().getModel().equals("slim");
    }

    /**
     * Whether the player is in the sneaking pose or not.
     *
     * @return Whether the player is in the sneaking pose or not.
     */
    public boolean isInSneakingPose() {
        return getEntity().isInSneakingPose();
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
