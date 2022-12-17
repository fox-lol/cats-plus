package xyz.foxkin.catsplus.mixin.commonloader.commonside.spraybottle;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.foxkin.catsplus.commonside.access.spraybottle.PlayerEntityAccess;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements PlayerEntityAccess {

    @Unique
    private int catsPlus$sprayBottleParticleCooldown = 0;

    @Inject(method = "tick", at = @At("HEAD"))
    private void catsPlus$decrement(CallbackInfo ci) {
        if (catsPlus$sprayBottleParticleCooldown > 0) {
            catsPlus$sprayBottleParticleCooldown--;
        }
    }

    @Unique
    @Override
    public boolean catsPlus$canSpawnSprayBottleParticles() {
        return catsPlus$sprayBottleParticleCooldown == 0;
    }

    @Unique
    @Override
    public void catsPlus$setSprayBottleParticleCooldown(int cooldownTicks) {
        catsPlus$sprayBottleParticleCooldown = cooldownTicks;
    }
}
