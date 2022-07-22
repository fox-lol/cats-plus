package xyz.foxkin.catsplus.client.animatable.player;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.jetbrains.annotations.Nullable;
import xyz.foxkin.catsplus.client.access.render.AnimatableContainer;

@Environment(EnvType.CLIENT)
public class FirstPersonPlayerArms extends PlayerArms {

    @Nullable
    public static FirstPersonPlayerArms instance;

    protected FirstPersonPlayerArms(ClientPlayerEntity player) {
        super(player, true);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void clearOtherPerspectiveAnimations() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            AnimatableContainer<ThirdPersonPlayerArms> container = (AnimatableContainer<ThirdPersonPlayerArms>) player;
            PlayerArms arms = container.catsPlus$getAnimatable();
            arms.setPendingAnimations();
        }
    }

    public static void createInstance(ClientPlayerEntity player) {
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
