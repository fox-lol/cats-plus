package xyz.foxkin.catsplus.mixin.commonloader.client.entitypickup;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import xyz.foxkin.catsplus.client.access.render.AnimatableContainer;
import xyz.foxkin.catsplus.client.animatable.mob.ReplacedCatEntity;

@SuppressWarnings("unused")
@Mixin(CatEntity.class)
abstract class CatEntityMixin extends TameableEntity implements AnimatableContainer<ReplacedCatEntity> {

    @Unique
    private final ReplacedCatEntity catsPlus$replacedCat = new ReplacedCatEntity((CatEntity) (Object) this);

    protected CatEntityMixin(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public ReplacedCatEntity catsPlus$getAnimatable() {
        return catsPlus$replacedCat;
    }
}
