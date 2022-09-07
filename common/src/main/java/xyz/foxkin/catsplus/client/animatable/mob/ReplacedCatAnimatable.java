package xyz.foxkin.catsplus.client.animatable.mob;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.util.Identifier;
import xyz.foxkin.catsplus.client.animatable.HoldableEntityAnimatable;

@Environment(EnvType.CLIENT)
public class ReplacedCatAnimatable extends HoldableEntityAnimatable<CatEntity> {

    public ReplacedCatAnimatable(CatEntity cat) {
        super(cat);
    }

    @Override
    public Identifier getTexture() {
        return getEntity().getTexture();
    }
}
