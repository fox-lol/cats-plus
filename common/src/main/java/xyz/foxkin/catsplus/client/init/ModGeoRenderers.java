package xyz.foxkin.catsplus.client.init;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import software.bernie.geckolib3.core.controller.AnimationController;
import xyz.foxkin.catsplus.client.animatable.CatsPlusAnimatable;
import xyz.foxkin.catsplus.client.animatable.mob.ReplacedCatEntity;
import xyz.foxkin.catsplus.client.animatable.player.FirstPersonPlayerArms;
import xyz.foxkin.catsplus.client.animatable.player.ThirdPersonPlayerArms;
import xyz.foxkin.catsplus.client.model.entity.CatsPlusModel;
import xyz.foxkin.catsplus.client.model.entity.mob.ReplacedCatModel;
import xyz.foxkin.catsplus.client.model.entity.player.FirstPersonPlayerArmsModel;
import xyz.foxkin.catsplus.client.model.entity.player.ThirdPersonPlayerArmsModel;
import xyz.foxkin.catsplus.client.render.CatsPlusGeoRenderer;
import xyz.foxkin.catsplus.client.render.entity.mob.ReplacedCatRenderer;
import xyz.foxkin.catsplus.client.render.entity.player.FirstPersonPlayerArmsRenderer;
import xyz.foxkin.catsplus.client.render.entity.player.ThirdPersonPlayerArmsRenderer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class ModGeoRenderers {

    private static final Map<Class<? extends CatsPlusAnimatable>, CatsPlusGeoRenderer<?, ?>> RENDERERS = new HashMap<>();

    public static void registerRenderers() {
        registerRenderer(FirstPersonPlayerArms.class, new FirstPersonPlayerArmsRenderer(new FirstPersonPlayerArmsModel()));
        registerRenderer(ThirdPersonPlayerArms.class, new ThirdPersonPlayerArmsRenderer(new ThirdPersonPlayerArmsModel()));
        registerRenderer(ReplacedCatEntity.class, new ReplacedCatRenderer(new ReplacedCatModel()));
    }

    private static <T extends CatsPlusAnimatable, S extends CatsPlusModel<T>> void registerRenderer(Class<T> animatableClass, CatsPlusGeoRenderer<T, S> renderer) {
        RENDERERS.put(animatableClass, renderer);
        AnimationController.addModelFetcher(renderer::modelFetcher);
    }

    @SuppressWarnings("unchecked")
    public static <T extends CatsPlusAnimatable, S extends CatsPlusModel<T>> Optional<CatsPlusGeoRenderer<T, S>> getRenderer(Class<T> animatableClass) {
        return Optional.ofNullable((CatsPlusGeoRenderer<T, S>) RENDERERS.get(animatableClass));
    }
}
