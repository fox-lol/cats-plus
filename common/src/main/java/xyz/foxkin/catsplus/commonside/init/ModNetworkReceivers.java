package xyz.foxkin.catsplus.commonside.init;

import dev.architectury.networking.NetworkManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import xyz.foxkin.catsplus.commonside.CatsPlus;
import xyz.foxkin.catsplus.commonside.access.entitypickup.PlayerEntityAccess;

public class ModNetworkReceivers {

    // Client to Server
    public static final Identifier SET_INTERACT_WITH_HELD_ENTITY = new Identifier(CatsPlus.MOD_ID, "set_interact_with_held_entity");

    // Server to client
    public static final Identifier PLAY_FIRST_PERSON_ARMS_ANIMATIONS = new Identifier(CatsPlus.MOD_ID, "play_first_person_arms_animation");
    public static final Identifier PLAY_ENTITY_ANIMATIONS = new Identifier(CatsPlus.MOD_ID, "play_entity_animation");
    public static final Identifier CANCEL_FIRST_PERSON_ARMS_ANIMATIONS = new Identifier(CatsPlus.MOD_ID, "cancel_first_person_arms_animation");
    public static final Identifier CANCEL_ANIMATIONS = new Identifier(CatsPlus.MOD_ID, "cancel_animations");
    public static final Identifier SYNC_HELD_ENTITY_TO_CLIENT = new Identifier(CatsPlus.MOD_ID, "sync_held_entity_to_client");

    public static void registerReceivers() {
        NetworkManager.registerReceiver(NetworkManager.c2s(), SET_INTERACT_WITH_HELD_ENTITY, (buf, context) -> {
            boolean interactWithHeldEntity = buf.readBoolean();
            PlayerEntity holder = context.getPlayer();
            context.queue(() -> {
                PlayerEntityAccess playerAccess = (PlayerEntityAccess) holder;
                playerAccess.catsPlus$setInteractingWithHeldEntity(interactWithHeldEntity);
            });
        });
    }
}
