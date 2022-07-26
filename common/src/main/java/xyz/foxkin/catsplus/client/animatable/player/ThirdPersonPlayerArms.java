package xyz.foxkin.catsplus.client.animatable.player;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class ThirdPersonPlayerArms extends PlayerArms {

    public ThirdPersonPlayerArms(AbstractClientPlayerEntity player) {
        super(player, false);
    }

    @Override
    protected void clearOtherPerspectiveAnimations() {
        if (Objects.equals(getEntity(), MinecraftClient.getInstance().player)) {
            PlayerArms arms = FirstPersonPlayerArms.getInstance();
            arms.setPendingAnimations();
        }
    }
}
