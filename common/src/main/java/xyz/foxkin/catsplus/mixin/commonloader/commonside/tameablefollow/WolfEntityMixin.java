package xyz.foxkin.catsplus.mixin.commonloader.commonside.tameablefollow;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.foxkin.catsplus.commonside.init.ModTags;

@Mixin(WolfEntity.class)
abstract class WolfEntityMixin extends TameableEntityMixin {

    @SuppressWarnings("unused")
    protected WolfEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * If the owner of a cat interacts with the cat while holding an item
     * tagged with {@link ModTags#TOGGLE_WOLF_FOLLOWING}, it will toggle
     * whether the cat follows the owner or not.
     */
    @Inject(method = "interactMob", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/TameableEntity;interactMob(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;"), slice = @Slice(
            from = @At(value = "FIELD", target = "Lnet/minecraft/util/ActionResult;SUCCESS:Lnet/minecraft/util/ActionResult;"),
            to = @At(value = "INVOKE", target = "Lnet/minecraft/util/ActionResult;isAccepted()Z")
    ), cancellable = true)
    private void catsPlus$ownerToggleFollowing(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isIn(ModTags.TOGGLE_WOLF_FOLLOWING)) {
            catsPlus$toggleFollowing();
            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }
}
