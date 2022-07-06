package xyz.foxkin.catsplus.mixin.commonloader.commonside.tameablefollow;

import net.minecraft.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.entity.passive.TameableEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.foxkin.catsplus.commonside.access.TameableEntityAccess;

@Mixin(FollowOwnerGoal.class)
abstract class FollowOwnerGoalMixin {

    @Shadow
    @Final
    private TameableEntity tameable;

    /**
     * A tameable entity will not start or continue to follow
     * its owner if it is not set to follow its owner.
     */
    @Inject(method = {"canStart", "shouldContinue"}, at = @At("HEAD"), cancellable = true)
    private void catsPlus$onlyStartIfFollowing(CallbackInfoReturnable<Boolean> cir) {
        TameableEntityAccess access = (TameableEntityAccess) tameable;
        if (!access.catsPlus$isFollowing()) {
            cir.setReturnValue(false);
        }
    }
}
