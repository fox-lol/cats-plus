package xyz.foxkin.catsplus.mixin.commonloader.commonside.accessor;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.sound.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MobEntity.class)
public interface MobEntityAccessor extends LivingEntityAccessor {

    @Invoker("resetSoundDelay")
    void catsPlus$invokeResetSoundDelay();

    @Invoker("getAmbientSound")
    SoundEvent catsPlus$invokeGetAmbientSound();
}
