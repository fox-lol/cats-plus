package xyz.foxkin.catsplus.mixin.commonloader.commonside.entitypickup;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.foxkin.catsplus.commonside.access.entitypickup.EntityAccess;
import xyz.foxkin.catsplus.commonside.access.entitypickup.PlayerEntityAccess;
import xyz.foxkin.catsplus.commonside.animation.AnimationSyncing;
import xyz.foxkin.catsplus.commonside.init.ModEntityHeldPoses;
import xyz.foxkin.catsplus.commonside.init.ModTags;

import java.util.Random;

@Mixin(Entity.class)
abstract class EntityMixin implements EntityAccess {

    @Unique
    private static final String CATS_PLUS$HELD_POSE_NBT_KEY = "catsplus:heldPose";
    @Unique
    private static final Random CATS_PLUS$RANDOM = new Random();

    @Unique
    private int catsPlus$heldPoseNumber = 0;

    @Shadow
    public abstract EntityType<?> getType();

    @Shadow
    public abstract void setPitch(float pitch);

    @Shadow
    public abstract void discard();

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
    private void catsPlus$pickupEntity(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        PlayerEntityAccess playerAccess = (PlayerEntityAccess) player;
        if (getType().isIn(ModTags.CAN_PICK_UP)
                && player.isSneaking()
                && player.getMainHandStack().isEmpty()
                && player.getOffHandStack().isEmpty()
                && playerAccess.catsPlus$getHeldEntity().isEmpty()
        ) {
            if ((Object) this instanceof TameableEntity thisTameable) {
                if (thisTameable.isTamed()) {
                    thisTameable.setSitting(false);
                } else {
                    return;
                }
            }

            int heldPosesCount = ModEntityHeldPoses.getEntityHeldPosesCount(getType());
            if (heldPosesCount > 0) {
                int heldPoseNumber;
                if (heldPosesCount == 1) {
                    heldPoseNumber = 1;
                } else {
                    heldPoseNumber = CATS_PLUS$RANDOM.nextInt(heldPosesCount - 1) + 1;
                }
                catsPlus$setHeldPoseNumber(heldPoseNumber);
            }

            setPitch(0);
            playerAccess.catsPlus$setHeldEntity((Entity) (Object) this);

            Identifier entityId = EntityType.getId(getType());
            boolean isBaby;
            if ((Object) this instanceof LivingEntity livingEntity) {
                isBaby = livingEntity.isBaby();
            } else {
                isBaby = false;
            }
            AnimationSyncing.syncArmsAnimationsFromServer(player, false,
                    "holding."
                            + (isBaby ? "baby." : "")
                            + entityId.getNamespace()
                            + "_" + entityId.getPath()
                            + ".picking_up."
                            + catsPlus$getHeldPoseNumber());

            discard();
            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    private void catsPlus$writeBeingHeld(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        nbt.putInt(CATS_PLUS$HELD_POSE_NBT_KEY, catsPlus$heldPoseNumber);
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    private void catsPlus$readBeingHeld(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains(CATS_PLUS$HELD_POSE_NBT_KEY, NbtElement.INT_TYPE)) {
            catsPlus$heldPoseNumber = nbt.getInt(CATS_PLUS$HELD_POSE_NBT_KEY);
        }
    }

    @Override
    public int catsPlus$getHeldPoseNumber() {
        return catsPlus$heldPoseNumber;
    }

    @Override
    public void catsPlus$setHeldPoseNumber(int heldPoseNumber) {
        catsPlus$heldPoseNumber = heldPoseNumber;
    }
}
