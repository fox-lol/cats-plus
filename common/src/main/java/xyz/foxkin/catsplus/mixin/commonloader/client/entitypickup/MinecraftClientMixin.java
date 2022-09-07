package xyz.foxkin.catsplus.mixin.commonloader.client.entitypickup;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.network.PacketByteBuf;
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
    @Nullable
    public ClientPlayerEntity player;

    @Shadow
    @Final
    public GameOptions options;

    /**
     * Prevents attacking while the player is holding an entity.
     */
    @Inject(method = "doAttack", at = @At("HEAD"), cancellable = true)
    private void catsPlus$preventAttackIfHoldingEntity(CallbackInfoReturnable<Boolean> cir) {
        if (catsPlus$isHoldingEntity()) {
            cir.setReturnValue(false);
        }
    }

    /**
     * Prevents breaking blocks while the player is holding an entity.
     */
    @Inject(method = "handleBlockBreaking", at = @At("HEAD"), cancellable = true)
    private void catsPlus$preventBlockBreakingIfHoldingEntity(boolean bl, CallbackInfo ci) {
        if (catsPlus$isHoldingEntity()) {
            ci.cancel();
        }
    }

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
