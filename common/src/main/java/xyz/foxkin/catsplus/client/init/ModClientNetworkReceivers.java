package xyz.foxkin.catsplus.client.init;

import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.world.World;
import xyz.foxkin.catsplus.client.access.render.AnimatableContainer;
import xyz.foxkin.catsplus.client.animatable.CatsPlusAnimatable;
import xyz.foxkin.catsplus.client.animatable.player.FirstPersonPlayerArms;
import xyz.foxkin.catsplus.client.animatable.player.PlayerArms;
import xyz.foxkin.catsplus.commonside.CatsPlus;
import xyz.foxkin.catsplus.commonside.access.entitypickup.PlayerEntityAccess;
import xyz.foxkin.catsplus.commonside.init.ModNetworkReceivers;

@Environment(EnvType.CLIENT)
public class ModClientNetworkReceivers {

    public static void registerReceivers() {
        // Plays first person arm animations.
        NetworkManager.registerReceiver(NetworkManager.serverToClient(), ModNetworkReceivers.PLAY_FIRST_PERSON_ARMS_ANIMATIONS, (buf, context) -> {
            AnimationData animationData = decodeAnimationData(buf);
            context.queue(() -> {
                PlayerArms firstPersonArms = FirstPersonPlayerArms.getInstance();
                firstPersonArms.setPendingAnimations(animationData.animationNames());
                firstPersonArms.setLastPendingAnimationShouldLoop(animationData.lastShouldLoop());
            });
        });

        // Plays entity animations if the entity contains an animatable component.
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
                        animatable.setPendingAnimations(animationData.animationNames());
                        animatable.setLastPendingAnimationShouldLoop(animationData.lastShouldLoop());
                    } else {
                        CatsPlus.LOGGER.error("Could not find an animatable entity with id {}", entityId);
                    }
                }
            });
        });

        // Syncs the held entity to the client.
        NetworkManager.registerReceiver(NetworkManager.serverToClient(), ModNetworkReceivers.SYNC_HELD_ENTITY_TO_CLIENT, (buf, context) -> {
            NbtCompound heldEntityNbt = buf.readNbt();
            PlayerEntity player = context.getPlayer();
            context.queue(() -> {
                if (player != null) {
                    PlayerEntityAccess playerAccess = (PlayerEntityAccess) player;
                    if (heldEntityNbt == null || heldEntityNbt.isEmpty()) {
                        playerAccess.catsPlus$clearHeldEntity();
                    } else {
                        EntityType.getEntityFromNbt(heldEntityNbt, player.getEntityWorld()).ifPresentOrElse(playerAccess::catsPlus$setHeldEntity, () -> {
                            CatsPlus.LOGGER.error("Could not create entity from nbt {}", heldEntityNbt);
                            playerAccess.catsPlus$clearHeldEntity();
                        });
                    }
                }
            });
        });
    }

    /**
     * Decodes animation data from a packet.
     *
     * @param buf The packet to decode from.
     * @return The decoded animation data.
     */
    private static AnimationData decodeAnimationData(PacketByteBuf buf) {
        boolean lastShouldLoop = buf.readBoolean();
        int animationCount = buf.readInt();
        String[] animationNames = new String[animationCount];
        for (int i = 0; i < animationNames.length; i++) {
            animationNames[i] = buf.readString();
        }
        return new AnimationData(lastShouldLoop, animationNames);
    }

    /**
     * Represents animation data.
     *
     * @param lastShouldLoop Whether the last animation should loop.
     * @param animationNames The names of the animations.
     */
    private record AnimationData(boolean lastShouldLoop, String[] animationNames) {
    }
}
