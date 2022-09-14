package xyz.foxkin.catsplus.mixin.commonloader.commonside.accessor;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.sound.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {

    @Invoker("getSoundVolume")
    float catsPlus$invokeGetSoundVolume();

    @Invoker("getHurtSound")
    SoundEvent catsPlus$invokeGetHurtSound(DamageSource source);
}
