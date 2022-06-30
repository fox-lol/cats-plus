package xyz.foxkin.catsplus.commonside.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import xyz.foxkin.catsplus.commonside.CatsPlus;

@SuppressWarnings("unused")
public class ModSounds {

    private static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(CatsPlus.MOD_ID, Registry.SOUND_EVENT_KEY);

    public static final RegistrySupplier<SoundEvent> CAT_LAUGHTER = SOUNDS.register(
            "cat_laughter",
            () -> new SoundEvent(new Identifier(CatsPlus.MOD_ID, "cat_laughter"))
    );

    public static void registerSounds() {
        SOUNDS.register();
    }
}
