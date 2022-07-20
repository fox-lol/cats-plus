package xyz.foxkin.catsplus.mixin.commonloader.commonside.accessor;

import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ActiveTargetGoal.class)
public interface ActiveTargetGoalAccessor {

    @Accessor("targetPredicate")
    TargetPredicate catsPlus$getTargetPredicate();
}
