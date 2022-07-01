package xyz.foxkin.catsplus.mixin.forge.commonside;

import net.minecraft.block.enums.Instrument;
import net.minecraft.sound.SoundEvent;
import net.minecraftforge.common.IExtensibleEnum;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.foxkin.catsplus.commonside.block.enums.CustomInstrument;

@Mixin(Instrument.class)
public class InstrumentMixin implements IExtensibleEnum {

    /**
     * Adds custom {@link Instrument} enum values.
     */
    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void catsplus$addCustomInstruments(CallbackInfo ci) {
        for (CustomInstrument instrument : CustomInstrument.values()) {
            create(instrument.getEnumName(), instrument.getInstrumentName(), instrument.getInstrumentSound());
        }
    }

    /**
     * Adds a custom {@link Instrument} enum value.
     * The contents of this method will be replaced by Forge at runtime.
     */
    @SuppressWarnings({"SameParameterValue", "unused", "UnusedReturnValue"})
    private static Instrument create(String enumName, String instrumentName, SoundEvent sound) {
        throw new IllegalStateException("Enum not extended");
    }
}
