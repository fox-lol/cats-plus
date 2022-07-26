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

    /**
     * Creates and sets the instance of this class.
     *
     * @param player The player the animatable belongs to.
     */
    public static void createInstance(ClientPlayerEntity player) {
        instance = new FirstPersonPlayerArms(player);
    }

    /**
     * Gets the instance of this class.
     *
     * @return The instance of this class.
     * @throws IllegalStateException If the instance has not been created yet.
     */
    public static FirstPersonPlayerArms getInstance() {
        if (instance == null) {
            throw new IllegalStateException(FirstPersonPlayerArms.class.getSimpleName() + " is not initialized");
        } else {
            return instance;
        }
    }

    /**
     * Clears the instance of this class.
     */
    public static void clearInstance() {
        instance = null;
    }
}
