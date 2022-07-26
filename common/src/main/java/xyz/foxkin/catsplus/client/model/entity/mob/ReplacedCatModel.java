package xyz.foxkin.catsplus.client.model.entity.mob;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import xyz.foxkin.catsplus.client.animatable.mob.ReplacedCatEntity;
import xyz.foxkin.catsplus.client.model.entity.CatsPlusModel;
import xyz.foxkin.catsplus.commonside.CatsPlus;

@Environment(EnvType.CLIENT)
public class ReplacedCatModel extends CatsPlusModel<ReplacedCatEntity> {

    public ReplacedCatModel() {
        super(CatsPlus.MOD_ID, "geo/entity/mob/replaced_cat.geo.json", "animations/entity/mob/replaced_cat.animation.json");
    }
}
