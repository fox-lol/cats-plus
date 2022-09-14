package xyz.foxkin.catsplus.mixin.commonloader.client.entitypickup;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import xyz.foxkin.catsplus.client.access.render.AnimatableContainer;
import xyz.foxkin.catsplus.client.animatable.entity.mob.ReplacedCatAnimatable;

@SuppressWarnings("unused")
@Mixin(CatEntity.class)
abstract class CatEntityMixin extends TameableEntity implements AnimatableContainer<ReplacedCatAnimatable> {

    @SuppressWarnings("ConstantConditions")
    @Unique
    private final ReplacedCatAnimatable catsPlus$replacedCat = new ReplacedCatAnimatable((CatEntity) (Object) this);

    protected CatEntityMixin(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    @Unique
    @Override
    public ReplacedCatAnimatable catsPlus$getAnimatable() {
        return catsPlus$replacedCat;
    }
}
