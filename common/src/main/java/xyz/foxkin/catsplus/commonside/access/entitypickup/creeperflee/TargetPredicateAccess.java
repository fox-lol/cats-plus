package xyz.foxkin.catsplus.commonside.access.entitypickup.creeperflee;

import net.minecraft.entity.LivingEntity;

import java.util.function.Predicate;

public interface TargetPredicateAccess {

    void catsPlus$andPredicate(Predicate<LivingEntity> predicate);
}
