package xyz.foxkin.catsplus.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import xyz.foxkin.catsplus.client.animatable.CatsPlusAnimatable;
import xyz.foxkin.catsplus.commonside.CatsPlus;

@Environment(EnvType.CLIENT)
public class CatsPlusModel<T extends CatsPlusAnimatable> extends AnimatedGeoModel<T> {

    private final Identifier model;
    private final Identifier animation;

    public CatsPlusModel(String modelPath, String animationPath) {
        model = new Identifier(CatsPlus.MOD_ID, modelPath);
        animation = new Identifier(CatsPlus.MOD_ID, animationPath);
    }

    @Override
    public Identifier getModelLocation(T animatable) {
        return model;
    }

    @Override
    public Identifier getTextureLocation(T animatable) {
        return animatable.getTexture();
    }

    @Override
    public Identifier getAnimationFileLocation(T animatable) {
        return animation;
    }

    /**
     * Sets up the model before it's rendered.
     *
     * @param animatable The animatable this model is for.
     */
    public void setUp(T animatable) {
        setLivingAnimations(animatable);
    }

    public void setLivingAnimations(T animatable) {
        setLivingAnimations(animatable, animatable.getUniqueId());
    }

    public GeoModel getModel(T animatable) {
        return getModel(getModelLocation(animatable));
    }
}
