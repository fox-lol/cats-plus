package xyz.foxkin.catsplus.client.animatable.player;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.EntityType;
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
            if (playerAccess.catsPlus$isHoldingEntity()) {
                playerAccess.catsPlus$getHeldEntity().ifPresent(entity -> {
                    EntityAccess entityAccess = (EntityAccess) entity;
                    int heldPoseNumber = entityAccess.catsPlus$getHeldPoseNumber();
                    if (heldPoseNumber > 0) {
                        Identifier entityId = EntityType.getId(entity.getType());
                        playAnimations(true, getAnimationPrefix() + "holding." + entityId.getNamespace() + "_" + entityId.getPath() + ".idle." + heldPoseNumber);
                    }
                });
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void playAnimations(boolean lastShouldLoop, String... animationNames) {
        super.playAnimations(lastShouldLoop, animationNames);
        clearOtherPerspectiveAnimations();
    }

    protected abstract void clearOtherPerspectiveAnimations();

    @Override
    public Identifier getTexture() {
        return getEntity().getSkinTexture();
    }

    public boolean isSlimArms() {
        return getEntity().getModel().equals("slim");
    }

    public boolean isInSneakingPose() {
        return getEntity().isInSneakingPose();
    }

    private String getAnimationPrefix() {
        return (firstPerson ? "first" : "third") + "_person.";
    }
}
