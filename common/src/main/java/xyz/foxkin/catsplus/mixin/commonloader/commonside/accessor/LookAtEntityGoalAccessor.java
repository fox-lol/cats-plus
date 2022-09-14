package xyz.foxkin.catsplus.mixin.commonloader.commonside.accessor;

import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LookAtEntityGoal.class)
public interface LookAtEntityGoalAccessor {

    @Accessor("targetPredicate")
    TargetPredicate catsPlus$getTargetPredicate();
}
