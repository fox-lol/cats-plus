package xyz.foxkin.catsplus.client.init;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import software.bernie.geckolib3.core.controller.AnimationController;
import xyz.foxkin.catsplus.client.animatable.CatsPlusAnimatable;
import xyz.foxkin.catsplus.client.animatable.mob.ReplacedCatEntity;
import xyz.foxkin.catsplus.client.animatable.player.PlayerArms;
import xyz.foxkin.catsplus.client.model.entity.mob.ReplacedCatModel;
import xyz.foxkin.catsplus.client.model.entity.player.PlayerArmsModel;
import xyz.foxkin.catsplus.client.render.CatsPlusGeoRenderer;
import xyz.foxkin.catsplus.client.render.entity.mob.ReplacedCatRenderer;
import xyz.foxkin.catsplus.client.render.entity.player.PlayerArmsRenderer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class ModGeoRenderers {

    private static final Map<Class<? extends CatsPlusAnimatable>, CatsPlusGeoRenderer<?>> RENDERERS = new HashMap<>();

    public static void registerRenderers() {
        registerRenderer(PlayerArms.class, new PlayerArmsRenderer(new PlayerArmsModel()));
        registerRenderer(ReplacedCatEntity.class, new ReplacedCatRenderer(new ReplacedCatModel()));
    }

    private static <T extends CatsPlusAnimatable> void registerRenderer(Class<T> clazz, CatsPlusGeoRenderer<T> renderer) {
        RENDERERS.put(clazz, renderer);
        AnimationController.addModelFetcher(renderer::modelFetcher);
    }

    @SuppressWarnings("unchecked")
    public static <T extends CatsPlusAnimatable> Optional<CatsPlusGeoRenderer<T>> getRenderer(Class<T> clazz) {
        return Optional.ofNullable((CatsPlusGeoRenderer<T>) RENDERERS.get(clazz));
    }
}
