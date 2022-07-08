package xyz.foxkin.catsplus.commonside.init;

import dev.architectury.networking.NetworkManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import xyz.foxkin.catsplus.commonside.CatsPlus;
import xyz.foxkin.catsplus.commonside.access.entitypickup.PlayerEntityAccess;

public class ModNetworkReceivers {

    public static final Identifier THROW_HELD_ENTITY = new Identifier(CatsPlus.MOD_ID, "throw_held_entity");

    public static void registerReceivers() {
        registerServerReceivers();
    }

    private static void registerServerReceivers() {
        NetworkManager.registerReceiver(NetworkManager.clientToServer(), THROW_HELD_ENTITY, (buf, context) -> {
            PlayerEntity player = context.getPlayer();
            context.queue(() -> {
                PlayerEntityAccess playerAccess = (PlayerEntityAccess) player;
                playerAccess.catsPlus$throwHeldEntity(1);
            });
        });
    }
}
