package xyz.foxkin.catsplus.commonside.animation;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import xyz.foxkin.catsplus.commonside.init.ModNetworkReceivers;
import xyz.foxkin.catsplus.commonside.util.PlayerLookup;

import java.util.Collection;

public class AnimationSyncing {

    public static void syncArmsAnimations(PlayerEntity player, boolean lastShouldLoop, String... animationNamesWithoutPrefix) {
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
            syncAnimationsToPlayers(player, lastShouldLoop, thirdPersonAnimationNames);
        }
    }

    public static void syncAnimationsToPlayers(Entity toBeAnimated, boolean lastShouldLoop, String... animationNames) {
        if (toBeAnimated.getWorld().isClient()) {
            throw new IllegalStateException("Cannot sync animations from the client");
        } else {
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            buf.writeInt(toBeAnimated.getId());
            writeAnimationData(buf, lastShouldLoop, animationNames);
            Collection<ServerPlayerEntity> trackingPlayers = PlayerLookup.tracking(toBeAnimated);
            if (toBeAnimated instanceof ServerPlayerEntity player) {
                NetworkManager.sendToPlayer(player, ModNetworkReceivers.PLAY_ENTITY_ANIMATIONS, buf);
            }
            for (ServerPlayerEntity player : trackingPlayers) {
                NetworkManager.sendToPlayer(player, ModNetworkReceivers.PLAY_ENTITY_ANIMATIONS, buf);
            }
        }
    }

    private static void writeAnimationData(PacketByteBuf buf, boolean lastShouldLoop, String... animationNames) {
        buf.writeBoolean(lastShouldLoop);
        buf.writeInt(animationNames.length);
        for (String animationName : animationNames) {
            buf.writeString(animationName);
        }
    }
}
