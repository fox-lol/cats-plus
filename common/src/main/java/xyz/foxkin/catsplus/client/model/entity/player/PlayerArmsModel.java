package xyz.foxkin.catsplus.client.model.entity.player;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import xyz.foxkin.catsplus.client.animatable.player.PlayerArms;
import xyz.foxkin.catsplus.client.model.entity.CatsPlusModel;
import xyz.foxkin.catsplus.commonside.CatsPlus;

@Environment(EnvType.CLIENT)
public class PlayerArmsModel extends CatsPlusModel<PlayerArms> {

    public PlayerArmsModel() {
        super(CatsPlus.MOD_ID, "geo/entity/player/entity_holding_arms.geo.json", "animations/entity/player/entity_holding_arms.animation.json");
    }

    @Override
    public void setUpModel(PlayerArms animatable) {
        super.setUpModel(animatable);
        hideParts();
        setArmThickness(animatable);
    }

    private void hideParts() {
        try {
            getBone("entity_placeholder").setHidden(true);
            getBone("body").setHidden(true);
        } catch (RuntimeException ignored) {
        }
    }

    /**
     * Sets the arm thickness of the model.
     *
     * @param playerArms The arms to set the thickness of.
     */
    private void setArmThickness(PlayerArms playerArms) {
        boolean slimArms = playerArms.isSlimArms();
        try {
            if (slimArms) {
                getBone("right_arm_wide").setHidden(true);
                getBone("left_arm_wide").setHidden(true);
                getBone("right_arm_layer_wide").setHidden(true);
                getBone("left_arm_layer_wide").setHidden(true);

                getBone("right_arm_slim").setHidden(false);
                getBone("left_arm_slim").setHidden(false);
                getBone("right_arm_layer_slim").setHidden(false);
                getBone("left_arm_layer_slim").setHidden(false);
            } else {
                getBone("right_arm_wide").setHidden(false);
                getBone("left_arm_wide").setHidden(false);
                getBone("right_arm_layer_wide").setHidden(false);
                getBone("left_arm_layer_wide").setHidden(false);

                getBone("right_arm_slim").setHidden(true);
                getBone("left_arm_slim").setHidden(true);
                getBone("right_arm_layer_slim").setHidden(true);
                getBone("left_arm_layer_slim").setHidden(true);
            }
        } catch (RuntimeException ignored) {
        }
    }
}
