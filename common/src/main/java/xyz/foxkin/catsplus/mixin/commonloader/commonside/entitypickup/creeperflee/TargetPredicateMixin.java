package xyz.foxkin.catsplus.mixin.commonloader.commonside.entitypickup.creeperflee;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import xyz.foxkin.catsplus.commonside.access.entitypickup.creeperflee.TargetPredicateAccess;

import java.util.function.Predicate;

@SuppressWarnings("unused")
@Mixin(TargetPredicate.class)
abstract class TargetPredicateMixin implements TargetPredicateAccess {

    @Shadow
    @Nullable
    private Predicate<LivingEntity> predicate;

    @Override
    public void catsPlus$andPredicate(Predicate<LivingEntity> newPredicate) {
        if (predicate == null) {
            predicate = newPredicate;
        } else {
            predicate = predicate.and(newPredicate);
        }
    }
}
