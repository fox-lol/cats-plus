package xyz.foxkin.catsplus.commonside.animation;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import xyz.foxkin.catsplus.client.animatable.player.PlayerArms;
import xyz.foxkin.catsplus.commonside.init.ModNetworkReceivers;
import xyz.foxkin.catsplus.commonside.util.PlayerLookup;

public class AnimationSyncing {

    /**
     * Syncs {@link PlayerArms} animations to clients.
     *
     * @param player                      The player the arms belong to.
     * @param lastShouldLoop              Whether the last animation should loop.
     * @param animationNamesWithoutPrefix The names of the animations without the perspective prefix.
     */
    public static void syncArmsAnimationsToClients(PlayerEntity player, boolean lastShouldLoop, String... animationNamesWithoutPrefix) {
        if (player.getWorld().isClient()) {
            throw new IllegalStateException("Cannot sync animations from the client");
        } else {
            String[] firstPersonAnimationNames = new String[animationNamesWithoutPrefix.length];
            String[] thirdPersonAnimationNames = new String[animationNamesWithoutPrefix.length];
            for (int i = 0; i < animationNamesWithoutPrefix.length; i++) {
                firstPersonAnimationNames[i] = "first_person." + animationNamesWithoutPrefix[i];
                thirdPersonAnimationNames[i] = "third_person." + animationNamesWithoutPrefix[i];
            }
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            writeAnimationData(buf, lastShouldLoop, firstPersonAnimationNames);
            NetworkManager.sendToPlayer((ServerPlayerEntity) player, ModNetworkReceivers.PLAY_FIRST_PERSON_ARMS_ANIMATIONS, buf);
            syncAnimationsToPlayersToClients(player, lastShouldLoop, thirdPersonAnimationNames);
        }
    }

    /**
     * Syncs entity animations to clients.
     *
     * @param toBeAnimated   The entity to be animated.
     * @param lastShouldLoop Whether the last animation should loop.
     * @param animationNames The names of the animations.
     */
    public static void syncAnimationsToPlayersToClients(Entity toBeAnimated, boolean lastShouldLoop, String... animationNames) {
        if (toBeAnimated.getWorld().isClient()) {
            throw new IllegalStateException("Cannot sync animations from the client");
        } else {
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            buf.writeInt(toBeAnimated.getId());
            writeAnimationData(buf, lastShouldLoop, animationNames);
            if (toBeAnimated instanceof ServerPlayerEntity toBeAnimatedPlayer) {
                NetworkManager.sendToPlayer(toBeAnimatedPlayer, ModNetworkReceivers.PLAY_ENTITY_ANIMATIONS, buf);
            }
            for (ServerPlayerEntity player : PlayerLookup.tracking(toBeAnimated)) {
                NetworkManager.sendToPlayer(player, ModNetworkReceivers.PLAY_ENTITY_ANIMATIONS, buf);
            }
        }
    }

    /**
     * Encodes the animation data into a packet.
     *
     * @param buf            The packet to write to.
     * @param lastShouldLoop Whether the last animation should loop.
     * @param animationNames The names of the animations.
     */
    private static void writeAnimationData(PacketByteBuf buf, boolean lastShouldLoop, String... animationNames) {
        buf.writeBoolean(lastShouldLoop);
        buf.writeInt(animationNames.length);
        for (String animationName : animationNames) {
            buf.writeString(animationName);
        }
    }
}
