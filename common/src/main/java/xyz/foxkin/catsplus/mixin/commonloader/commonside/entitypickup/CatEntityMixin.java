package xyz.foxkin.catsplus.mixin.commonloader.commonside.entitypickup;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.foxkin.catsplus.commonside.access.entitypickup.PlayerEntityAccess;

@Mixin(CatEntity.class)
abstract class CatEntityMixin extends TameableEntity {

    @SuppressWarnings("unused")
    protected CatEntityMixin(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * If a player is sneaking and interacts with a tamed cat that is theirs,
     * the player will pick up the cat.
     */
    @Inject(method = "interactMob", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/CatEntity;setSitting(Z)V"), slice = @Slice(
            from = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/CatEntity;isSitting()Z"),
            to = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I")
    ), cancellable = true)
    private void catsPlus$ownerPickupCat(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        catsPlus$pickupCat(player, cir);
    }

    /**
     * If a player is sneaking and interacts with a tamed cat that is not theirs,
     * the player will pick up the cat.
     */
    @Inject(method = "interactMob", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/CatEntity;isTamed()Z", shift = At.Shift.AFTER), slice = @Slice(
            from = @At(value = "FIELD", target = "Lnet/minecraft/util/ActionResult;PASS:Lnet/minecraft/util/ActionResult;"),
            to = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;isFood()Z")
    ), cancellable = true)
    private void catsPlus$nonOwnerPickupCat(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (!isOwner(player)) {
            catsPlus$pickupCat(player, cir);
        }
    }

    /**
     * Picks up a cat.
     */
    private void catsPlus$pickupCat(PlayerEntity player, CallbackInfoReturnable<ActionResult> cir) {
        if (player.isSneaking()) {
            PlayerEntityAccess playerAccess = (PlayerEntityAccess) player;
            if (!playerAccess.catsPlus$isHoldingEntity()) {
                setSitting(false);
                NbtCompound nbtCompound = new NbtCompound();
                nbtCompound.putString("id", getSavedEntityId());
                writeNbt(nbtCompound);
                playerAccess.catsPlus$setHeldEntity(nbtCompound);
                discard();
                cir.setReturnValue(ActionResult.SUCCESS);
            }
        }
    }
}
