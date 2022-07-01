package xyz.foxkin.catsplus.commonside.block.enums;

import net.minecraft.block.enums.Instrument;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import java.util.function.Supplier;

/**
 * Represents a custom {@link Instrument}. Values added here are automatically added to the {@code Instrument} enum.
 */
public enum CustomInstrument {

    CAT("CATSPLUS$CAT", "catsplus_cat", () -> SoundEvents.ENTITY_CAT_AMBIENT);

    private final String ENUM_NAME;
    private final String INSTRUMENT_NAME;
    private final Supplier<SoundEvent> INSTRUMENT_SOUND_SUPPLIER;

    /**
     * Constructs a new {@code CustomInstrument}.
     *
     * @param enumName                The name of the enum value that will be added to {@link Instrument}.
     * @param instrumentName          The name of the instrument.
     * @param instrumentSoundSupplier The {@code Supplier} for the sound event for the instrument.
     *                                A {@code Supplier} is needed to prevent premature classloading of {@code SoundEvent}
     */
    @SuppressWarnings("SameParameterValue")
    CustomInstrument(String enumName, String instrumentName, Supplier<SoundEvent> instrumentSoundSupplier) {
        ENUM_NAME = enumName;
        INSTRUMENT_NAME = instrumentName;
        INSTRUMENT_SOUND_SUPPLIER = instrumentSoundSupplier;
    }

    /**
     * Gets the {@code Instrument} enum value corresponding to this custom instrument.
     *
     * @return The {@code Instrument} enum value corresponding to this custom instrument.
     */
    public Instrument getInstrument() {
        return Instrument.valueOf(ENUM_NAME);
    }

    public String getEnumName() {
        return ENUM_NAME;
    }

    public String getInstrumentName() {
        return INSTRUMENT_NAME;
    }

    public SoundEvent getInstrumentSound() {
        return INSTRUMENT_SOUND_SUPPLIER.get();
    }
}
