package xyz.foxkin.catsplus.mixin.commonloader.client.entitypickup;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.foxkin.catsplus.client.access.render.AnimatableContainer;
import xyz.foxkin.catsplus.client.animatable.mob.ReplacedCatEntity;

@SuppressWarnings("unused")
@Mixin(CatEntity.class)
abstract class CatEntityMixin implements AnimatableContainer<ReplacedCatEntity> {

    private ReplacedCatEntity catsPlus$replacedCat;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void catsPlus$setAnimatable(EntityType<? extends CatEntity> entityType, World world, CallbackInfo ci) {
        catsPlus$replacedCat = new ReplacedCatEntity((CatEntity) (Object) this);
    }

    @Override
    public ReplacedCatEntity catsPlus$getAnimatable() {
        return catsPlus$replacedCat;
    }
}
