package xyz.foxkin.catsplus.mixin.commonloader.commonside.accessor;

import net.minecraft.entity.ai.goal.CatSitOnBlockGoal;
import net.minecraft.entity.passive.CatEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CatSitOnBlockGoal.class)
public interface CatSitOnBlockGoalAccessor {

    @Accessor("cat")
    CatEntity catsPlus$getCat();
}
