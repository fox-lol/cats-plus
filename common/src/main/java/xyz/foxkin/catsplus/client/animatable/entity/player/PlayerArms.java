package xyz.foxkin.catsplus.client.animatable.entity.player;

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
import xyz.foxkin.catsplus.client.animatable.entity.EntityAnimatable;
import xyz.foxkin.catsplus.commonside.access.entitypickup.PlayerEntityAccess;

import java.util.Objects;

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
                        playAnimations(controller, transitionLengthTicks, true,
                                "holding."
                                        + (isBaby ? "baby." : "")
                                        + entityId.getNamespace()
                                        + "_"
                                        + entityId.getPath()
                                        + ".interacting."
                                        + heldPoseNumber
                        );
                    } else if (controller.getAnimationState() == AnimationState.Stopped || currentAnimationName.contains("interacting")) {
                        playAnimations(controller, transitionLengthTicks, true,
                                "holding."
                                        + (isBaby ? "baby." : "")
                                        + entityId.getNamespace()
                                        + "_"
                                        + entityId.getPath()
                                        + ".idle."
                                        + heldPoseNumber
                        );
                    }
                }
            });
            return PlayState.CONTINUE;
        }
    }

    @Override
    protected <T extends CatsPlusAnimatable> PlayState constantAnimationPredicate(AnimationEvent<T> event) {
        PlayState playState = super.constantAnimationPredicate(event);
        if (playState == PlayState.STOP) {
            return playState;
        } else {
            AnimationController<?> controller = event.getController();
            PlayerEntityAccess playerAccess = (PlayerEntityAccess) getEntity();
            playerAccess.catsPlus$getHeldEntity().ifPresent(heldEntity -> {
                int heldPoseNumber = playerAccess.catsPlus$getHeldPoseNumber();
                if (heldPoseNumber > 0) {
                    Identifier entityId = EntityType.getId(heldEntity.getType());
                    boolean isBaby = heldEntity instanceof LivingEntity livingEntity && livingEntity.isBaby();
                    playAnimations(controller, 0, true,
                            "holding."
                                    + (isBaby ? "baby." : "")
                                    + entityId.getNamespace()
                                    + "_"
                                    + entityId.getPath()
                                    + ".constant."
                                    + heldPoseNumber
                    );
                }
            });
            return PlayState.CONTINUE;
        }
    }

    @Override
    public Identifier getTexture() {
        return getEntity().getSkinTexture();
    }

    @Override
    public int getUniqueId() {
        return Objects.hash(super.getUniqueId(), firstPerson);
    }
}
