package xyz.foxkin.catsplus.client.model.entity.player;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.PlayerModelPart;
import software.bernie.geckolib3.core.processor.IBone;
import xyz.foxkin.catsplus.client.animatable.player.PlayerArms;
import xyz.foxkin.catsplus.client.model.entity.CatsPlusModel;

@Environment(EnvType.CLIENT)
public abstract class PlayerArmsModel<T extends PlayerArms> extends CatsPlusModel<T> {

    public PlayerArmsModel(String modelPath, String animationPath) {
        super(modelPath, animationPath);
    }

    @Override
    public void setUp(T animatable) {
        tickOtherPerspectiveAnimations(animatable);
        super.setUp(animatable);
        hideParts();
        setArmThickness(animatable);
    }

    /**
     * Continues playing the other perspective's animations even when not being rendered
     * in order to prevent animations from replaying when the player changes perspective.
     *
     * @param currentPerspective The animatable representing the current perspective.
     */
    protected abstract void tickOtherPerspectiveAnimations(T currentPerspective);

    private void hideParts() {
        setBoneHidden("entity_placeholder", true);
        setBoneHidden("body", true);
    }

    /**
     * Sets the arm thickness of the model.
     *
     * @param playerArms The arms to set the thickness of.
     */
    private void setArmThickness(PlayerArms playerArms) {
        boolean slimArms = playerArms.getEntity().getModel().equals("slim");

        setBoneHidden("right_arm_wide", slimArms);
        setBoneHidden("left_arm_wide", slimArms);
        setBoneHidden("right_arm_layer_wide", slimArms || !MinecraftClient.getInstance().options.isPlayerModelPartEnabled(PlayerModelPart.RIGHT_SLEEVE));
        setBoneHidden("left_arm_layer_wide", slimArms || !MinecraftClient.getInstance().options.isPlayerModelPartEnabled(PlayerModelPart.LEFT_SLEEVE));

        setBoneHidden("right_arm_slim", !slimArms);
        setBoneHidden("left_arm_slim", !slimArms);
        setBoneHidden("right_arm_layer_slim", !slimArms || !MinecraftClient.getInstance().options.isPlayerModelPartEnabled(PlayerModelPart.RIGHT_SLEEVE));
        setBoneHidden("left_arm_layer_slim", !slimArms || !MinecraftClient.getInstance().options.isPlayerModelPartEnabled(PlayerModelPart.LEFT_SLEEVE));
    }

    private void setBoneHidden(String boneName, boolean hidden) {
        IBone bone = getAnimationProcessor().getBone(boneName);
        if (bone != null) {
            bone.setHidden(hidden);
        }
    }
}
