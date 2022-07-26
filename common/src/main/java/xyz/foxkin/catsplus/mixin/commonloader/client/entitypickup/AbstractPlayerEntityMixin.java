package xyz.foxkin.catsplus.mixin.commonloader.client.entitypickup;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.encryption.PlayerPublicKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.foxkin.catsplus.client.access.render.AnimatableContainer;
import xyz.foxkin.catsplus.client.animatable.player.ThirdPersonPlayerArms;

@SuppressWarnings("unused")
@Mixin(AbstractClientPlayerEntity.class)
abstract class AbstractPlayerEntityMixin implements AnimatableContainer<ThirdPersonPlayerArms> {

    private ThirdPersonPlayerArms catsPlus$playerArms;

    /**
     * Sets the animatable instance.
     */
    @Inject(method = "<init>", at = @At("RETURN"))
    private void catsPlus$setAnimatable(ClientWorld world, GameProfile profile, PlayerPublicKey publicKey, CallbackInfo ci) {
        catsPlus$playerArms = new ThirdPersonPlayerArms((AbstractClientPlayerEntity) (Object) this);
    }

    @Override
    public ThirdPersonPlayerArms catsPlus$getAnimatable() {
        return catsPlus$playerArms;
    }
}
