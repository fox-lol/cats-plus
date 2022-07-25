package xyz.foxkin.catsplus.mixin.commonloader.client.entitypickup;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.foxkin.catsplus.commonside.access.entitypickup.PlayerEntityAccess;

@Mixin(MinecraftClient.class)
abstract class MinecraftClientMixin {

    @Shadow
    @Nullable
    public ClientPlayerEntity player;

    @Inject(method = "doAttack", at = @At("HEAD"), cancellable = true)
    private void catsPlus$cancelAttackIfHoldingEntity(CallbackInfoReturnable<Boolean> cir) {
        if (catsPlus$isHoldingEntity()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "handleBlockBreaking", at = @At("HEAD"), cancellable = true)
    private void catsPlus$cancelBlockBreakingIfHoldingEntity(boolean bl, CallbackInfo ci) {
        if (catsPlus$isHoldingEntity()) {
            ci.cancel();
        }
    }

    private boolean catsPlus$isHoldingEntity() {
        if (player != null) {
            PlayerEntityAccess playerAccess = (PlayerEntityAccess) player;
            return playerAccess.catsPlus$getHeldEntity().isPresent();
        }
        return false;
    }
}
