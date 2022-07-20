package xyz.foxkin.catsplus.mixin.commonloader.commonside.entitypickup;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.foxkin.catsplus.commonside.access.entitypickup.PlayerEntityAccess;

// TODO: Replace with events on Forge.
@Mixin(ServerPlayerEntity.class)
abstract class ServerPlayerEntityMixin extends PlayerEntity implements PlayerEntityAccess {

    @SuppressWarnings("unused")
    protected ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile, @Nullable PlayerPublicKey publicKey) {
        super(world, pos, yaw, gameProfile, publicKey);
    }

    /**
     * Copies the held entity from the old player.
     */
    @Inject(method = "copyFrom", at = @At("HEAD"))
    private void catsPlus$copyHeldEntity(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        PlayerEntityAccess oldPlayerAccess = (PlayerEntityAccess) oldPlayer;
        catsPlus$setHeldEntityNbt(oldPlayerAccess.catsPlus$getHeldEntityNbt());
    }

    /**
     * The player drops its held entity when the player dies.
     */
    @Inject(method = "onDeath", at = @At("HEAD"))
    private void catsPlus$dropHeldEntityOnDeath(DamageSource damageSource, CallbackInfo ci) {
        catsPlus$dropHeldEntity(getPos());
    }

    /**
     * The player drops its held entity when the player enters spectator mode.
     */
    @Inject(method = "changeGameMode", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;dropShoulderEntities()V"))
    private void catsPlus$dropHeldEntityInSpectator(GameMode gameMode, CallbackInfoReturnable<Boolean> cir) {
        catsPlus$dropHeldEntity(getPos());
    }

    @Inject(method = "dropSelectedItem", at = @At("HEAD"))
    private void catsPlus$throwHeldEntity(boolean entireStack, CallbackInfoReturnable<Boolean> cir) {
        if (catsPlus$isHoldingEntity()) {
            catsPlus$throwHeldEntity(1);
        }
    }
}
