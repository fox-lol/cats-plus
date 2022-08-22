package xyz.foxkin.catsplus.commonside.animation;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import xyz.foxkin.catsplus.client.animatable.player.PlayerArms;
import xyz.foxkin.catsplus.commonside.init.ModNetworkReceivers;
import xyz.foxkin.catsplus.commonside.util.PlayerLookup;

public class AnimationSyncing {

    /**
     * Syncs {@link PlayerArms} animations to clients.
     *
     * @param armsOwner                   The player the arms belong to.
     * @param transitionLengthTicks       The length of the animation transition.
     * @param lastShouldLoop              Whether the last animation should loop.
     * @param animationNamesWithoutPrefix The names of the animations without the perspective prefix.
     */
    public static void syncArmsAnimationsToClients(PlayerEntity armsOwner, int transitionLengthTicks, boolean lastShouldLoop, String... animationNamesWithoutPrefix) {
        if (armsOwner.getWorld().isClient()) {
            throw new IllegalStateException("Cannot sync animations from the client");
        } else {
            String[] firstPersonAnimationNames = new String[animationNamesWithoutPrefix.length];
            String[] thirdPersonAnimationNames = new String[animationNamesWithoutPrefix.length];
            for (int i = 0; i < animationNamesWithoutPrefix.length; i++) {
                firstPersonAnimationNames[i] = "first_person." + animationNamesWithoutPrefix[i];
                thirdPersonAnimationNames[i] = "third_person." + animationNamesWithoutPrefix[i];
            }
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            writeAnimationData(buf, transitionLengthTicks, lastShouldLoop, firstPersonAnimationNames);
            NetworkManager.sendToPlayer((ServerPlayerEntity) armsOwner, ModNetworkReceivers.PLAY_FIRST_PERSON_ARMS_ANIMATIONS, buf);
            syncAnimationsToClients(armsOwner, transitionLengthTicks, lastShouldLoop, thirdPersonAnimationNames);
        }
    }

    public static void cancelArmsAnimations(PlayerEntity armsOwner) {
        if (armsOwner.getWorld().isClient()) {
            throw new IllegalStateException("Cannot cancel animations from the client");
        } else {
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            NetworkManager.sendToPlayer((ServerPlayerEntity) armsOwner, ModNetworkReceivers.CANCEL_FIRST_PERSON_ARMS_ANIMATIONS, buf);
            cancelAnimations(armsOwner);
        }
    }

    public static void cancelAnimations(Entity animatable) {
        if (animatable.getWorld().isClient()) {
            throw new IllegalStateException("Cannot cancel animations from the client");
        } else {
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            buf.writeInt(animatable.getId());
            sendToTrackingPlayers(animatable, ModNetworkReceivers.CANCEL_ANIMATIONS, buf);
        }
    }

    /**
     * Syncs entity animations to clients.
     *
     * @param animatable            The entity to be animated.
     * @param transitionLengthTicks The length of the animation transition.
     * @param lastShouldLoop        Whether the last animation should loop.
     * @param animationNames        The names of the animations.
     */
    public static void syncAnimationsToClients(Entity animatable, int transitionLengthTicks, boolean lastShouldLoop, String... animationNames) {
        if (animatable.getWorld().isClient()) {
            throw new IllegalStateException("Cannot sync animations from the client");
        } else {
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            buf.writeInt(animatable.getId());
            writeAnimationData(buf, transitionLengthTicks, lastShouldLoop, animationNames);
            sendToTrackingPlayers(animatable, ModNetworkReceivers.PLAY_ENTITY_ANIMATIONS, buf);
        }
    }

    private static void sendToTrackingPlayers(Entity entity, Identifier packetId, PacketByteBuf buf) {
        if (entity instanceof ServerPlayerEntity player) {
            NetworkManager.sendToPlayer(player, packetId, buf);
        }
        for (ServerPlayerEntity player : PlayerLookup.tracking(entity)) {
            NetworkManager.sendToPlayer(player, packetId, buf);
        }
    }

    /**
     * Encodes the animation data into a packet.
     *
     * @param buf                   The packet to write to.
     * @param transitionLengthTicks The length of the animation transition.
     * @param lastShouldLoop        Whether the last animation should loop.
     * @param animationNames        The names of the animations.
     */
    private static void writeAnimationData(PacketByteBuf buf, int transitionLengthTicks, boolean lastShouldLoop, String... animationNames) {
        buf.writeInt(transitionLengthTicks);
        buf.writeBoolean(lastShouldLoop);
        buf.writeInt(animationNames.length);
        for (String animationName : animationNames) {
            buf.writeString(animationName);
        }
    }
}
