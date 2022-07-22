package xyz.foxkin.catsplus.client.init;

import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.world.World;
import xyz.foxkin.catsplus.client.access.render.AnimatableContainer;
import xyz.foxkin.catsplus.client.animatable.CatsPlusAnimatable;
import xyz.foxkin.catsplus.client.animatable.player.FirstPersonPlayerArms;
import xyz.foxkin.catsplus.client.animatable.player.PlayerArms;
import xyz.foxkin.catsplus.commonside.CatsPlus;
import xyz.foxkin.catsplus.commonside.init.ModNetworkReceivers;

@Environment(EnvType.CLIENT)
public class ModClientNetworkReceivers {

    public static void registerReceivers() {
        NetworkManager.registerReceiver(NetworkManager.serverToClient(), ModNetworkReceivers.PLAY_FIRST_PERSON_ARMS_ANIMATIONS, (buf, context) -> {
            AnimationData animationData = decodeAnimationData(buf);
            context.queue(() -> {
                PlayerArms firstPersonArms = FirstPersonPlayerArms.getInstance();
                firstPersonArms.setPendingAnimations(animationData.animationNames);
                firstPersonArms.setLastPendingAnimationShouldLoop(animationData.lastShouldLoop);
            });
        });

        NetworkManager.registerReceiver(NetworkManager.serverToClient(), ModNetworkReceivers.PLAY_ENTITY_ANIMATIONS, (buf, context) -> {
            int entityId = buf.readInt();
            AnimationData animationData = decodeAnimationData(buf);
            context.queue(() -> {
                World world = MinecraftClient.getInstance().world;
                if (world == null) {
                    CatsPlus.LOGGER.error("World is null, this shouldn't happen");
                } else {
                    Entity entity = world.getEntityById(entityId);
                    if (entity instanceof AnimatableContainer<?> container) {
                        CatsPlusAnimatable animatable = container.catsPlus$getAnimatable();
                        animatable.setPendingAnimations(animationData.animationNames);
                        animatable.setLastPendingAnimationShouldLoop(animationData.lastShouldLoop);
                    } else {
                        CatsPlus.LOGGER.error("Could not find an animatable entity with id {}", entityId);
                    }
                }
            });
        });
    }

    private static AnimationData decodeAnimationData(PacketByteBuf buf) {
        boolean lastShouldLoop = buf.readBoolean();
        int animationCount = buf.readInt();
        String[] animationNames = new String[animationCount];
        for (int i = 0; i < animationNames.length; i++) {
            animationNames[i] = buf.readString();
        }
        return new AnimationData(lastShouldLoop, animationNames);
    }

    private record AnimationData(boolean lastShouldLoop, String[] animationNames) {
    }
}
