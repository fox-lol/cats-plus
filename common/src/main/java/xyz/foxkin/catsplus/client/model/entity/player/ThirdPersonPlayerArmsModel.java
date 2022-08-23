package xyz.foxkin.catsplus.client.model.entity.player;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import xyz.foxkin.catsplus.client.animatable.player.FirstPersonPlayerArms;
import xyz.foxkin.catsplus.client.animatable.player.ThirdPersonPlayerArms;
import xyz.foxkin.catsplus.client.init.ModGeoRenderers;
import xyz.foxkin.catsplus.client.model.entity.CatsPlusModel;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class ThirdPersonPlayerArmsModel extends PlayerArmsModel<ThirdPersonPlayerArms> {

    @Override
    protected void tickOtherPerspectiveAnimations(ThirdPersonPlayerArms currentPerspective) {
        if (Objects.equals(currentPerspective.getEntity(), MinecraftClient.getInstance().player)) {
            var renderer = ModGeoRenderers.getRenderer(FirstPersonPlayerArms.class).orElseThrow();
            FirstPersonPlayerArms animatable = FirstPersonPlayerArms.getInstance();
            CatsPlusModel<FirstPersonPlayerArms> model = renderer.getGeoModelProvider();
            model.setLivingAnimations(animatable, animatable.getUniqueId());
        }
    }
}
