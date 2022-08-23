package xyz.foxkin.catsplus.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimatableModel;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;
import xyz.foxkin.catsplus.client.animatable.CatsPlusAnimatable;
import xyz.foxkin.catsplus.client.init.ModGeoRenderers;
import xyz.foxkin.catsplus.client.model.entity.CatsPlusModel;

@Environment(EnvType.CLIENT)
public abstract class CatsPlusGeoRenderer<T extends CatsPlusAnimatable, S extends CatsPlusModel<T>> implements IGeoRenderer<T> {

    private final S modelProvider;
    private final Class<T> animatableClass;

    public CatsPlusGeoRenderer(S modelProvider, Class<T> animatableClass) {
        this.modelProvider = modelProvider;
        this.animatableClass = animatableClass;
    }

    public void render(T animatable, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        modelProvider.setUpModel(animatable);
        GeoModel model = getGeoModel(animatable);
        RenderLayer renderLayer = getRenderType(animatable, 0, matrices, vertexConsumers, null, light, getTextureResource(animatable));
        render(model, animatable, 0, renderLayer, matrices, vertexConsumers, null, light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
    }

    @Override
    public S getGeoModelProvider() {
        return modelProvider;
    }

    @Override
    public Identifier getTextureResource(T animatable) {
        return animatable.getTexture();
    }

    @Override
    public Integer getUniqueID(T animatable) {
        return animatable.getUniqueId();
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public IAnimatableModel<Object> modelFetcher(IAnimatable animatable) {
        if (animatableClass.isInstance(animatable)) {
            return ModGeoRenderers.getRenderer(animatableClass)
                    .map(renderer -> (CatsPlusGeoRenderer<?, ?>) renderer)
                    .map(renderer -> (IAnimatableModel<Object>) renderer.getGeoModelProvider())
                    .orElse(null);
        } else {
            return null;
        }
    }

    public GeoModel getGeoModel(T animatable) {
        return modelProvider.getModel(modelProvider.getModelResource(animatable));
    }
}
