package xyz.foxkin.catsplus.mixin.commonloader.client.entitypickup;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import xyz.foxkin.catsplus.client.access.render.AnimatableContainer;
import xyz.foxkin.catsplus.client.animatable.entity.player.ThirdPersonPlayerArms;

@SuppressWarnings("unused")
@Mixin(AbstractClientPlayerEntity.class)
abstract class AbstractPlayerEntityMixin implements AnimatableContainer<ThirdPersonPlayerArms> {

    @SuppressWarnings("ConstantConditions")
    @Unique
    private final ThirdPersonPlayerArms catsPlus$playerArms = new ThirdPersonPlayerArms((AbstractClientPlayerEntity) (Object) this);

    @Override
    public ThirdPersonPlayerArms catsPlus$getAnimatable() {
        return catsPlus$playerArms;
    }
}
