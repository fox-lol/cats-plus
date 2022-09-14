package xyz.foxkin.catsplus.mixin.commonloader.commonside.goal;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.foxkin.catsplus.commonside.entity.ai.goal.CatSleepInBedGoal;

@Mixin(CatEntity.class)
abstract class CatEntityMixin extends TameableEntity {

    @SuppressWarnings("unused")
    protected CatEntityMixin(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * Adds custom goals to the cat's AI.
     */
    @SuppressWarnings("ConstantConditions")
    @Inject(method = "initGoals", at = @At("HEAD"))
    private void catsPlus$addCatUseFurnitureGoal(CallbackInfo ci) {
        goalSelector.add(7, new CatSleepInBedGoal((CatEntity) (Object) this, 0.8));
    }
}
