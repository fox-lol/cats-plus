package xyz.foxkin.catsplus.mixin.commonloader.client.entitypickup;

import net.minecraft.entity.passive.CatEntity;
import org.spongepowered.asm.mixin.Mixin;
import xyz.foxkin.catsplus.client.access.render.AnimatableContainer;
import xyz.foxkin.catsplus.client.animatable.mob.ReplacedCatEntity;

@SuppressWarnings("unused")
@Mixin(CatEntity.class)
abstract class CatEntityMixin implements AnimatableContainer<ReplacedCatEntity> {

    private final ReplacedCatEntity catsPlus$replacedCat = new ReplacedCatEntity((CatEntity) (Object) this);

    @Override
    public ReplacedCatEntity catsPlus$getIAnimatable() {
        return catsPlus$replacedCat;
    }
}
