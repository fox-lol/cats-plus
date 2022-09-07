package xyz.foxkin.catsplus.client.model.entity.mob;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import xyz.foxkin.catsplus.client.animatable.mob.ReplacedCatAnimatable;
import xyz.foxkin.catsplus.client.model.entity.CatsPlusModel;

@Environment(EnvType.CLIENT)
public class ReplacedCatModel extends CatsPlusModel<ReplacedCatAnimatable> {

    public ReplacedCatModel() {
        super("geo/entity/mob/replaced_cat.geo.json", "animations/entity/mob/replaced_cat.animation.json");
    }
}
