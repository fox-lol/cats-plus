package xyz.foxkin.catsplus.mixin.commonloader.commonside.entitypickup.creeperflee;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.foxkin.catsplus.commonside.access.entitypickup.PlayerEntityAccess;
import xyz.foxkin.catsplus.commonside.access.entitypickup.creeperflee.TargetPredicateAccess;
import xyz.foxkin.catsplus.mixin.commonloader.commonside.accessor.ActiveTargetGoalAccessor;
import xyz.foxkin.catsplus.mixin.commonloader.commonside.accessor.LookAtEntityGoalAccessor;

import java.util.function.Predicate;

@Mixin(CreeperEntity.class)
abstract class CreeperEntityMixin extends HostileEntity {

    @SuppressWarnings("unused")
    protected CreeperEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * Creepers flee from player's holding a cat or ocelot.
     */
    @Inject(method = "initGoals", at = @At("HEAD"))
    private void catsPlus$addFleeFromPlayerHoldingCatGoal(CallbackInfo ci) {
        goalSelector.add(3, new FleeEntityGoal<>(
                this,
                PlayerEntity.class,
                6,
                1,
                1.2,
                EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.and(this::catsPlus$isPlayerHoldingCat)::test
        ));
    }

    /**
     * Creepers don't look at players holding a cat or ocelot.
     */
    @SuppressWarnings("unused")
    @ModifyExpressionValue(method = "initGoals", at = @At(value = "NEW", target = "net/minecraft/entity/ai/goal/LookAtEntityGoal"))
    private LookAtEntityGoal catsPlus$onlyLookIfNotHoldingCat(LookAtEntityGoal goal) {
        LookAtEntityGoalAccessor goalAccessor = (LookAtEntityGoalAccessor) goal;
        TargetPredicate targetPredicate = goalAccessor.catsPlus$getTargetPredicate();
        TargetPredicateAccess predicateAccess = (TargetPredicateAccess) targetPredicate;
        Predicate<LivingEntity> newPredicate = this::catsPlus$isPlayerHoldingCat;
        newPredicate = newPredicate.negate();
        predicateAccess.catsPlus$andPredicate(newPredicate);
        return goal;
    }

    /**
     * Creepers don't target players holding a cat or ocelot.
     */
    @SuppressWarnings("unused")
    @ModifyExpressionValue(method = "initGoals", at = @At(value = "NEW", target = "net/minecraft/entity/ai/goal/ActiveTargetGoal"))
    private <T extends LivingEntity> ActiveTargetGoal<T> catsPlus$onlyTargetIfNotHoldingCat(ActiveTargetGoal<T> goal) {
        ActiveTargetGoalAccessor goalAccessor = (ActiveTargetGoalAccessor) goal;
        TargetPredicate targetPredicate = goalAccessor.catsPlus$getTargetPredicate();
        TargetPredicateAccess predicateAccess = (TargetPredicateAccess) targetPredicate;
        Predicate<LivingEntity> newPredicate = this::catsPlus$isPlayerHoldingCat;
        newPredicate = newPredicate.negate();
        predicateAccess.catsPlus$andPredicate(newPredicate);
        return goal;
    }

    /**
     * Checks if an entity is a player and is holding a cat or ocelot.
     *
     * @param entity The entity to check.
     * @return Whether the entity is a player and is holding a cat or ocelot.
     */
    @Unique
    private boolean catsPlus$isPlayerHoldingCat(Entity entity) {
        if (entity instanceof PlayerEntityAccess playerAccess) {
            return playerAccess.catsPlus$getHeldEntity()
                    .map(heldEntity -> heldEntity instanceof OcelotEntity || heldEntity instanceof CatEntity)
                    .orElse(false);
        } else {
            return false;
        }
    }
}
