package xyz.foxkin.catsplus.client.render.entity.player;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimatableModel;
import xyz.foxkin.catsplus.client.animatable.player.PlayerArms;
import xyz.foxkin.catsplus.client.init.ModGeoRenderers;
import xyz.foxkin.catsplus.client.model.entity.CatsPlusModel;
import xyz.foxkin.catsplus.client.render.CatsPlusGeoRenderer;

@Environment(EnvType.CLIENT)
public class PlayerArmsRenderer extends CatsPlusGeoRenderer<PlayerArms> {

    public PlayerArmsRenderer(CatsPlusModel<PlayerArms> modelProvider) {
        super(modelProvider);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public IAnimatableModel<Object> modelFetcher(IAnimatable animatable) {
        if (animatable instanceof PlayerArms) {
            CatsPlusGeoRenderer<PlayerArms> renderer = ModGeoRenderers.getRenderer(PlayerArms.class).orElseThrow();
            return (IAnimatableModel<Object>) renderer.getGeoModelProvider();
        } else {
            return null;
        }
    }
}
