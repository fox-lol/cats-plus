package xyz.foxkin.catsplus.commonside.entity.ai.goal;

import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import xyz.foxkin.catsplus.commonside.access.spraybottle.CatEntityAccess;

public class SprayDangerGoal extends EscapeDangerGoal {

    public SprayDangerGoal(PathAwareEntity mob, double speed) {
        super(mob, speed);
    }

    @Override
    protected boolean isInDanger() {
        CatEntityAccess access = (CatEntityAccess) this.mob;
        return access.catsPlus$getFleeTicks() > 0 || super.isInDanger();
    }
}
