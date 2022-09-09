package xyz.foxkin.catsplus.client.model.entity.player;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import xyz.foxkin.catsplus.client.animatable.entity.player.PlayerArms;
import xyz.foxkin.catsplus.client.model.CatsPlusModel;

@Environment(EnvType.CLIENT)
public abstract class PlayerArmsModel<T extends PlayerArms> extends CatsPlusModel<T> {

    public PlayerArmsModel(String modelPath, String animationPath) {
        super(modelPath, animationPath);
    }

    @Override
    public void setUp(T animatable) {
        tickOtherPerspectiveAnimations(animatable);
        super.setUp(animatable);
    }

    /**
     * Continues playing the other perspective's animations even when not being rendered
     * in order to prevent animations from replaying when the player changes perspective.
     *
     * @param currentPerspective The animatable representing the current perspective.
     */
    protected abstract void tickOtherPerspectiveAnimations(T currentPerspective);
}
