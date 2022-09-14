package xyz.foxkin.catsplus.client.animatable.entity.player;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;

@Environment(EnvType.CLIENT)
public class ThirdPersonPlayerArms extends PlayerArms {

    public ThirdPersonPlayerArms(AbstractClientPlayerEntity player) {
        super(player, false);
    }
}
