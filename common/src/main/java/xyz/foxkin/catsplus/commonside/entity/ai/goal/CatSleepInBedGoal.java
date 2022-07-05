package xyz.foxkin.catsplus.commonside.entity.ai.goal;

import net.minecraft.block.Block;
import net.minecraft.entity.ai.goal.CatSitOnBlockGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.tag.TagKey;
import xyz.foxkin.catsplus.commonside.access.CatSitOnBlockGoalAccess;
import xyz.foxkin.catsplus.commonside.init.ModTags;
import xyz.foxkin.catsplus.mixin.commonloader.commonside.accessor.CatSitOnBlockGoalAccessor;
import xyz.foxkin.catsplus.mixin.commonloader.commonside.catsitonblock.CatSitOnBlockGoalMixin;

/**
 * A {@link Goal} that makes cats sleep on blocks tagged with {@link ModTags#CAT_SLEEP_ON}.
 * See {@link CatSitOnBlockGoalMixin} for additional implementation.
 */
public class CatSleepInBedGoal extends CatSitOnBlockGoal implements CatSitOnBlockGoalAccess {

    public CatSleepInBedGoal(CatEntity cat, double speed) {
        super(cat, speed);
    }

    @Override
    public boolean catsPlus$extraStartCondition() {
        CatSitOnBlockGoalAccessor accessor = (CatSitOnBlockGoalAccessor) this;
        return !accessor.catsPlus$getCat().isInSittingPose();
    }

    @Override
    public TagKey<Block> catsPlus$getBlockTag() {
        return ModTags.CAT_SLEEP_ON;
    }

    @Override
    public void catsPlus$setInPose(boolean inPose) {
        CatSitOnBlockGoalAccessor accessor = (CatSitOnBlockGoalAccessor) this;
        accessor.catsPlus$getCat().setInSleepingPose(inPose);
    }
}
