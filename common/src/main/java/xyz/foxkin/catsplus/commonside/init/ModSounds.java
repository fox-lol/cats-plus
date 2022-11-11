package xyz.foxkin.catsplus.commonside.init;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import xyz.foxkin.catsplus.commonside.CatsPlus;

public class ModSounds {

    private static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(CatsPlus.MOD_ID, Registry.SOUND_EVENT_KEY);

    public static final RegistrySupplier<SoundEvent> CAT_LAUGHTER = SOUNDS.register(
            "cat_laughter",
            () -> new SoundEvent(new Identifier(CatsPlus.MOD_ID, "cat_laughter"))
    );

    public static final RegistrySupplier<SoundEvent> SPRAY = SOUNDS.register(
            "spray",
            () -> new SoundEvent(new Identifier(CatsPlus.MOD_ID, "spray"))
    );

    public static void registerSounds() {
        SOUNDS.register();
    }
}
