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
import xyz.foxkin.catsplus.client.animatable.entity.player.FirstPersonPlayerArms;
import xyz.foxkin.catsplus.client.animatable.entity.player.PlayerArms;
import xyz.foxkin.catsplus.commonside.CatsPlus;
import xyz.foxkin.catsplus.commonside.access.entitypickup.PlayerEntityAccess;
import xyz.foxkin.catsplus.commonside.init.ModNetworkReceivers;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Environment(EnvType.CLIENT)
public class ModClientNetworkReceivers {

    public static void registerReceivers() {
        // Plays first person arm animations.
        NetworkManager.registerReceiver(NetworkManager.serverToClient(), ModNetworkReceivers.PLAY_FIRST_PERSON_ARMS_ANIMATIONS, (buf, context) -> {
            AnimationData animationData = decodeAnimationData(buf);
            context.queue(() -> {
                PlayerArms firstPersonArms = FirstPersonPlayerArms.getInstance();
                firstPersonArms.setPendingAnimations(animationData.transitionLengthTicks(), animationData.lastShouldLoop(), animationData.animationNames());
            });
        });

        // Plays entity animations if the entity contains an animatable component.
        NetworkManager.registerReceiver(NetworkManager.serverToClient(), ModNetworkReceivers.PLAY_ENTITY_ANIMATIONS, (buf, context) -> {
            int entityId = buf.readInt();
            AnimationData animationData = decodeAnimationData(buf);
            context.queue(() -> {
                World world = MinecraftClient.getInstance().world;
                if (world != null) {
                    Entity entity = world.getEntityById(entityId);
                    if (entity instanceof AnimatableContainer<?> container) {
                        CatsPlusAnimatable animatable = container.catsPlus$getAnimatable();
                        animatable.setPendingAnimations(animationData.transitionLengthTicks(), animationData.lastShouldLoop(), animationData.animationNames());
                    } else {
                        CatsPlus.LOGGER.error("Entity {} is not an animatable container", entity);
                    }
                }
            });
        });

        // Cancels first person arm animations.
        NetworkManager.registerReceiver(NetworkManager.serverToClient(), ModNetworkReceivers.CANCEL_FIRST_PERSON_ARMS_ANIMATIONS, (buf, context) -> context.queue(() -> {
            PlayerArms firstPersonArms = FirstPersonPlayerArms.getInstance();
            firstPersonArms.cancelAnimations();
        }));

        // Cancels entity animations.
        NetworkManager.registerReceiver(NetworkManager.serverToClient(), ModNetworkReceivers.CANCEL_ANIMATIONS, (buf, context) -> {
            int entityId = buf.readInt();
            context.queue(() -> {
                World world = MinecraftClient.getInstance().world;
                if (world != null) {
                    Entity entity = world.getEntityById(entityId);
                    if (entity instanceof AnimatableContainer<?> container) {
                        CatsPlusAnimatable animatable = container.catsPlus$getAnimatable();
                        animatable.cancelAnimations();
                    } else {
                        CatsPlus.LOGGER.error("Entity {} is not an animatable container", entity);
                    }
                }
            });
        });

        // Syncs the held entity to the client.
        NetworkManager.registerReceiver(NetworkManager.serverToClient(), ModNetworkReceivers.SYNC_HELD_ENTITY_TO_CLIENT, (buf, context) -> {
            UUID holdingPlayerUuid = buf.readUuid();
            NbtCompound newHeldEntityNbt = buf.readNbt();
            int heldPoseNumber = buf.readInt();
            context.queue(() -> {
                World world = MinecraftClient.getInstance().world;
                if (world != null) {
                    PlayerEntity holdingPlayer = world.getPlayerByUuid(holdingPlayerUuid);
                    if (holdingPlayer != null && newHeldEntityNbt != null) {
                        PlayerEntityAccess playerAccess = (PlayerEntityAccess) holdingPlayer;
                        Optional<UUID> currentHeldEntityUuidOptional = playerAccess.catsPlus$getHeldEntity().map(Entity::getUuid);
                        Optional<UUID> newHeldEntityUuidOptional = Optional.of(newHeldEntityNbt).map(nbt -> {
                            if (nbt.containsUuid("UUID")) {
                                return nbt.getUuid("UUID");
                            } else {
                                return null;
                            }
                        });
                        if (!Objects.equals(currentHeldEntityUuidOptional, newHeldEntityUuidOptional)) {
                            if (newHeldEntityNbt.isEmpty()) {
                                playerAccess.catsPlus$clearHeldEntity();
                            } else {
                                EntityType.getEntityFromNbt(newHeldEntityNbt, holdingPlayer.getWorld()).ifPresentOrElse(entity -> {
                                    playerAccess.catsPlus$setHeldEntity(entity);
                                    playerAccess.catsPlus$setHeldPoseNumber(heldPoseNumber);
                                }, () -> {
                                    CatsPlus.LOGGER.error("Could not create entity from nbt {}", newHeldEntityNbt);
                                    playerAccess.catsPlus$clearHeldEntity();
                                });
                            }
                        }
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
        int transitionLengthTicks = buf.readInt();
        boolean lastShouldLoop = buf.readBoolean();
        int animationCount = buf.readInt();
        String[] animationNames = new String[animationCount];
        for (int i = 0; i < animationNames.length; i++) {
            animationNames[i] = buf.readString();
        }
        return new AnimationData(transitionLengthTicks, lastShouldLoop, animationNames);
    }

    /**
     * Represents animation data.
     *
     * @param lastShouldLoop Whether the last animation should loop.
     * @param animationNames The names of the animations.
     */
    private record AnimationData(int transitionLengthTicks, boolean lastShouldLoop, String[] animationNames) {
    }
}
