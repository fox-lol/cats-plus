package xyz.foxkin.catsplus.mixin.commonloader.client.entitypickup;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.hit.HitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.foxkin.catsplus.commonside.access.entitypickup.PlayerEntityAccess;
import xyz.foxkin.catsplus.commonside.init.ModNetworkReceivers;

@Mixin(MinecraftClient.class)
abstract class MinecraftClientMixin {

    @Shadow
    @Final
    public GameOptions options;
    @Shadow
    @Nullable
    public ClientPlayerEntity player;
    @Shadow
    @Nullable
    public HitResult crosshairTarget;

    /**
     * Cancels attacking while the player is holding an entity.
     */
    @Inject(method = "doAttack", at = @At("HEAD"), cancellable = true)
    private void catsPlus$cancelAttackIfHoldingEntity(CallbackInfoReturnable<Boolean> cir) {
        if (catsPlus$isHoldingEntity()) {
            cir.setReturnValue(false);
        }
    }

    /**
     * Cancels breaking blocks while the player is holding an entity.
     */
    @Inject(method = "handleBlockBreaking", at = @At("HEAD"), cancellable = true)
    private void catsPlus$cancelBlockBreakingIfHoldingEntity(boolean bl, CallbackInfo ci) {
        if (catsPlus$isHoldingEntity()) {
            ci.cancel();
        }
    }

    /**
     * Cancels non block interactions while the player is holding an entity.
     */
    @Inject(method = "doItemUse", at = @At("HEAD"), cancellable = true)
    private void catsPlus$cancelNonBlockInteractIfHoldingEntity(CallbackInfo ci) {
        if (catsPlus$isHoldingEntity() && crosshairTarget != null && crosshairTarget.getType() != HitResult.Type.BLOCK) {
            ci.cancel();
        }
    }

    /**
     * Sends a packet to the server letting it know if the player is
     * interacting with it's held entity if the attack key is pressed.
     */
    @Inject(method = "handleInputEvents", at = @At("HEAD"))
    private void catsPlus$setInteractWithHeldEntity(CallbackInfo ci) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBoolean(options.attackKey.isPressed() && catsPlus$isHoldingEntity());
        NetworkManager.sendToServer(ModNetworkReceivers.SET_INTERACT_WITH_HELD_ENTITY, buf);
    }

    /**
     * Whether the player is holding an entity.
     *
     * @return Whether the player is holding an entity.
     */
    @Unique
    private boolean catsPlus$isHoldingEntity() {
        if (player == null) {
            return false;
        } else {
            PlayerEntityAccess playerAccess = (PlayerEntityAccess) player;
            return playerAccess.catsPlus$isHoldingEntity();
        }
    }
}
