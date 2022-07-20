package xyz.foxkin.catsplus.client.animatable.player;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class FirstPersonPlayerArms extends PlayerArms {

    @Nullable
    public static FirstPersonPlayerArms instance;

    public FirstPersonPlayerArms(AbstractClientPlayerEntity player) {
        super(player, true);
    }

    public static void createInstance(AbstractClientPlayerEntity player) {
        instance = new FirstPersonPlayerArms(player);
    }

    public static FirstPersonPlayerArms getInstance() {
        if (instance == null) {
            throw new IllegalStateException(FirstPersonPlayerArms.class.getSimpleName() + " is not initialized");
        } else {
            return instance;
        }
    }

    public static void clearInstance() {
        instance = null;
    }
}
