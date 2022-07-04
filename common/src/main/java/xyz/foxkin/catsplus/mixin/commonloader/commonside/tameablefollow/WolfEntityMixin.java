package xyz.foxkin.catsplus.mixin.commonloader.commonside.tameablefollow;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WolfEntity.class)
abstract class WolfEntityMixin extends TameableEntityMixin {

    @SuppressWarnings("unused")
    protected WolfEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * If the owner of a wolf interacts with the wolf while sneaking,
     * it will toggle whether the wolf follows the owner or not.
     */
    @Inject(method = "interactMob", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/WolfEntity;setSitting(Z)V", ordinal = 0), cancellable = true)
    private void catsPlus$setFollowing(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (player.isSneaking()) {
            catsPlus$toggleFollowing();
            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }
}
