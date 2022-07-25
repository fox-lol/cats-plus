package xyz.foxkin.catsplus.commonside.init;

import net.minecraft.util.Identifier;
import xyz.foxkin.catsplus.commonside.CatsPlus;

public class ModNetworkReceivers {

    public static final Identifier PLAY_FIRST_PERSON_ARMS_ANIMATIONS = new Identifier(CatsPlus.MOD_ID, "play_first_person_arms_animation");
    public static final Identifier PLAY_ENTITY_ANIMATIONS = new Identifier(CatsPlus.MOD_ID, "play_entity_animation");
    public static final Identifier SYNC_HELD_ENTITY_TO_CLIENT = new Identifier(CatsPlus.MOD_ID, "sync_held_entity_to_client");

    /**
     * Registers the logical server receivers.
     */
    public static void registerReceivers() {
    }
}
