package xyz.foxkin.catsplus.mixin.commonloader.client.entitypickup;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.foxkin.catsplus.commonside.access.entitypickup.PlayerEntityAccess;
import xyz.foxkin.catsplus.commonside.init.ModNetworkReceivers;

@Mixin(MinecraftClient.class)
abstract class MinecraftClientMixin {

    @Shadow @Nullable public ClientPlayerEntity player;

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "doAttack", at = @At("HEAD"), cancellable = true)
    private void catsPlus$throwHeldEntity(CallbackInfoReturnable<Boolean> cir) {
        PlayerEntityAccess playerAccess = (PlayerEntityAccess) player;
        if (playerAccess.catsPlus$isHoldingEntity()) {
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            NetworkManager.sendToServer(ModNetworkReceivers.THROW_HELD_ENTITY, buf);
            playerAccess.catsPlus$clearHeldEntity();
            cir.setReturnValue(false);
        }
    }
}
