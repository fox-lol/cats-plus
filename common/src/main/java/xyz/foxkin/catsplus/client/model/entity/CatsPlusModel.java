package xyz.foxkin.catsplus.client.model.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import xyz.foxkin.catsplus.client.animatable.CatsPlusAnimatable;

@Environment(EnvType.CLIENT)
public class CatsPlusModel<T extends CatsPlusAnimatable> extends AnimatedGeoModel<T> {

    private final Identifier model;
    @Nullable
    private Identifier texture;
    private final Identifier animation;

    public CatsPlusModel(String namespace, String modelPath, String animationPath) {
        model = new Identifier(namespace, modelPath);
        animation = new Identifier(namespace, animationPath);
    }

    @Override
    public Identifier getModelResource(T animatable) {
        return model;
    }

    @Override
    public Identifier getTextureResource(T animatable) {
        if (texture == null) {
            throw new IllegalStateException("Texture is not set");
        } else {
            return texture;
        }
    }

    @Override
    public Identifier getAnimationResource(T animatable) {
        return animation;
    }

    /**
     * Sets up the model before it's rendered.
     *
     * @param animatable The animatable this model is for.
     */
    public void setUpModel(T animatable) {
        setTexture(animatable.getTexture());
        setLivingAnimations(animatable, animatable.getUniqueId());
    }

    /**
     * Sets {@code Identifier} pointing to the location of the texture of this model.
     *
     * @param texture The location of the texture of this model.
     */
    private void setTexture(Identifier texture) {
        if (texture == null) {
            throw new IllegalArgumentException("Texture cannot be null");
        } else {
            this.texture = texture;
        }
    }
}
