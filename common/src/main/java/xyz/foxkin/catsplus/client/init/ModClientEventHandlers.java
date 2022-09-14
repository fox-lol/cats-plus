package xyz.foxkin.catsplus.client.init;

import dev.architectury.event.events.client.ClientPlayerEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import xyz.foxkin.catsplus.client.animatable.entity.player.FirstPersonPlayerArms;

@Environment(EnvType.CLIENT)
public class ModClientEventHandlers {

    public static void registerEventHandlers() {
        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(FirstPersonPlayerArms::createInstance);
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register(player -> FirstPersonPlayerArms.clearInstance());
        ClientPlayerEvent.CLIENT_PLAYER_RESPAWN.register((oldPlayer, newPlayer) -> FirstPersonPlayerArms.createInstance(newPlayer));
    }
}
