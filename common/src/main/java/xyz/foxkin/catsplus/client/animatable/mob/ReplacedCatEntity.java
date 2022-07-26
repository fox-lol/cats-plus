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

    /**
     * Whether the cat is tamed or not.
     *
     * @return Whether the cat is tamed or not.
     */
    public boolean isTamed() {
        return getEntity().isTamed();
    }

    /**
     * Gets an array of size 3 containing the red, green, and blue color components of the cat's collar.
     *
     * @return The red, green, and blue color components of the cat's collar.
     */
    public float[] getCollarColor() {
        return getEntity().getCollarColor().getColorComponents();
    }
}
