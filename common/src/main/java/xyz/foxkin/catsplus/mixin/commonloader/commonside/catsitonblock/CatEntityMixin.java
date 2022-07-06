package xyz.foxkin.catsplus.mixin.commonloader.commonside.catsitonblock;

import net.minecraft.entity.passive.CatEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.foxkin.catsplus.commonside.access.CatEntityAccess;

@Mixin(CatEntity.class)
abstract class CatEntityMixin implements CatEntityAccess {

    @Unique
    private static final String CATS_PLUS$SIT_OR_SLEEP_COOLDOWN_NBT_KEY = "catsplus:sitOrSleepCooldown";

    @Unique
    private int catsPlus$sitOrSleepCooldown = 0;

    @Inject(method = "tick", at = @At("HEAD"))
    private void catsPlus$decrementCooldown(CallbackInfo ci) {
        if (catsPlus$sitOrSleepCooldown > 0) {
            catsPlus$sitOrSleepCooldown--;
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    private void catsPlus$writeSitOrSleepCooldown(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt(CATS_PLUS$SIT_OR_SLEEP_COOLDOWN_NBT_KEY, Math.max(catsPlus$sitOrSleepCooldown, 0));
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    private void catsPlus$readSitOrSleepCooldown(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains(CATS_PLUS$SIT_OR_SLEEP_COOLDOWN_NBT_KEY, NbtElement.INT_TYPE)) {
            catsPlus$sitOrSleepCooldown = Math.max(nbt.getInt(CATS_PLUS$SIT_OR_SLEEP_COOLDOWN_NBT_KEY), 0);
        }
    }

    @Unique
    @Override
    public boolean catsPlus$canSitOrSleep() {
        return catsPlus$sitOrSleepCooldown <= 0;
    }

    @Unique
    @Override
    public void catsPlus$setSitOrSleepCooldown(int sitOrSleepCooldown) {
        catsPlus$sitOrSleepCooldown = Math.max(sitOrSleepCooldown, 0);
    }
}
