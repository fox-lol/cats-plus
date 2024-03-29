package xyz.foxkin.catsplus.mixin.commonloader.commonside.entitypickup;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.foxkin.catsplus.commonside.access.entitypickup.PlayerEntityAccess;

// TODO: Replace with events on Forge.
@Mixin(ServerPlayerEntity.class)
abstract class ServerPlayerEntityMixin extends PlayerEntityMixin {

    @SuppressWarnings("unused")
    protected ServerPlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * Copies the held entity from the old player.
     */
    @Inject(method = "copyFrom", at = @At("HEAD"))
    private void catsPlus$copyHeldEntity(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        PlayerEntityAccess oldPlayerAccess = (PlayerEntityAccess) oldPlayer;
        oldPlayerAccess.catsPlus$getHeldEntity().ifPresent(entity -> {
            catsPlus$setHeldEntity(entity);
            catsPlus$setHeldPoseNumber(oldPlayerAccess.catsPlus$getHeldPoseNumber());
        });
    }

    /**
     * The player drops its held entity when the player dies.
     */
    @Inject(method = "onDeath", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;dropShoulderEntities()V"))
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

    /**
     * Throws the player's held entity.
     */
    @Inject(method = "dropSelectedItem", at = @At("HEAD"))
    private void catsPlus$throwHeldEntity(boolean entireStack, CallbackInfoReturnable<Boolean> cir) {
        catsPlus$throwHeldEntity(1);
    }
}
