package xyz.foxkin.catsplus.mixin.commonloader.commonside.entitypickup;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.foxkin.catsplus.commonside.access.entitypickup.EntityAccess;
import xyz.foxkin.catsplus.commonside.access.entitypickup.PlayerEntityAccess;
import xyz.foxkin.catsplus.commonside.animation.AnimationSyncing;
import xyz.foxkin.catsplus.commonside.init.ModTags;

import java.util.Optional;

@Mixin(Entity.class)
abstract class EntityMixin implements EntityAccess {

    @Unique
    @Nullable
    private PlayerEntity catsPlus$holder;

    @Shadow
    public abstract EntityType<?> getType();

    @Shadow
    public abstract void setPitch(float pitch);

    @Shadow
    public abstract void discard();

    @Shadow
    public abstract World getWorld();

    /**
     * A player will pick up an entity if they are sneaking when they interact with it
     * and the entity is set to be able to be picked up.
     */
    @SuppressWarnings("ConstantConditions")
    @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
    private void catsPlus$pickupEntity(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        PlayerEntityAccess playerAccess = (PlayerEntityAccess) player;
        if (getType().isIn(ModTags.CAN_PICK_UP)
                && player.isSneaking()
                && player.getMainHandStack().isEmpty()
                && player.getOffHandStack().isEmpty()
                && !playerAccess.catsPlus$isHoldingEntity()
                && !((Object) this instanceof PlayerEntity)
        ) {
            if ((Object) this instanceof TameableEntity thisTameable) {
                if (thisTameable.isTamed()) {
                    thisTameable.setSitting(false);
                } else {
                    return;
                }
            }

            setPitch(0);
            playerAccess.catsPlus$setHeldEntity((Entity) (Object) this, 0);

            Identifier entityId = EntityType.getId(getType());
            boolean isBaby;
            if ((Object) this instanceof LivingEntity livingEntity) {
                isBaby = livingEntity.isBaby();
            } else {
                isBaby = false;
            }

            if (!getWorld().isClient()) {
                AnimationSyncing.cancelArmsAnimations(player);
                AnimationSyncing.syncArmsAnimationsToClients(player, 0, false,
                        "holding."
                                + (isBaby ? "baby." : "")
                                + entityId.getNamespace()
                                + "_"
                                + entityId.getPath()
                                + ".picking_up."
                                + playerAccess.catsPlus$getHeldPoseNumber());
            }

            discard();
            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }

    @Unique
    @Override
    public Optional<PlayerEntity> catsPlus$getHolder() {
        return Optional.ofNullable(catsPlus$holder);
    }

    @Unique
    @Override
    public void catsPlus$setHolder(PlayerEntity holder) {
        catsPlus$holder = holder;
    }
}
