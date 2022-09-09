package xyz.foxkin.catsplus.client.animatable.entity.player;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerEntity;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class FirstPersonPlayerArms extends PlayerArms {

    @Nullable
    public static FirstPersonPlayerArms instance;

    protected FirstPersonPlayerArms(ClientPlayerEntity player) {
        super(player, true);
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
