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
public class PlayerArms extends EntityAnimatable<AbstractClientPlayerEntity> {

    private final boolean firstPerson;

    public PlayerArms(AbstractClientPlayerEntity player, boolean firstPerson) {
        super(player);
        this.firstPerson = firstPerson;
    }

    @Override
    protected <T extends IAnimatable> PlayState animationPredicate(AnimationEvent<T> event) {
        if (event.getController().getAnimationState() != AnimationState.Running) {
            PlayerEntityAccess playerAccess = (PlayerEntityAccess) getEntity();
            if (playerAccess.catsPlus$isHoldingEntity()) {
                playerAccess.catsPlus$getHeldEntity().ifPresent(entity -> {
                    EntityAccess entityAccess = (EntityAccess) entity;
                    int heldPoseNumber = entityAccess.catsPlus$getHeldPoseNumber();
                    if (heldPoseNumber > 0) {
                        Identifier entityId = EntityType.getId(entity.getType());
                        playAnimations(true, "holding." + entityId.getNamespace() + "_" + entityId.getPath() + ".idle." + heldPoseNumber);
                    }
                });
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void playAnimations(boolean lastShouldLoop, String... animationNames) {
        for (int i = 0; i < animationNames.length; i++) {
            String animationName = animationNames[i];
            animationNames[i] = (firstPerson ? "first" : "third") + "_person." + animationName;
        }
        super.playAnimations(lastShouldLoop, animationNames);
    }

    @Override
    public Identifier getTexture() {
        return getEntity().getSkinTexture();
    }
}
