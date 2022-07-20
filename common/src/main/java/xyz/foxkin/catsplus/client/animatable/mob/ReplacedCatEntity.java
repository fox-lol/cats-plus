package xyz.foxkin.catsplus.client.animatable.mob;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.util.Identifier;
import xyz.foxkin.catsplus.client.animatable.HoldableEntityAnimatable;

@Environment(EnvType.CLIENT)
public class ReplacedCatEntity extends HoldableEntityAnimatable<CatEntity> {

    public ReplacedCatEntity(CatEntity cat) {
        super(cat);
    }

    @Override
    public Identifier getTexture() {
        return getEntity().getTexture();
    }

    public boolean isTamed() {
        return getEntity().isTamed();
    }

    public float[] getCollarColor() {
        return getEntity().getCollarColor().getColorComponents();
    }
}
