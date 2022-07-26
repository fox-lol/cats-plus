package xyz.foxkin.catsplus.commonside.access.entitypickup.creeperflee;

import net.minecraft.entity.LivingEntity;

import java.util.function.Predicate;

public interface TargetPredicateAccess {

    /**
     * Sets the predicate to be the result of the AND operation of the current predicate and the new predicate.
     *
     * @param predicate The new predicate.
     */
    void catsPlus$andPredicate(Predicate<LivingEntity> predicate);
}
