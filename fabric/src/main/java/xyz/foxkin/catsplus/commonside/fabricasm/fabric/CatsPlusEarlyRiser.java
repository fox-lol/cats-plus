package xyz.foxkin.catsplus.commonside.fabricasm.fabric;

import com.chocohead.mm.api.ClassTinkerers;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import net.minecraft.block.enums.Instrument;
import xyz.foxkin.catsplus.commonside.block.enums.CustomInstrument;

public class CatsPlusEarlyRiser implements Runnable {

    /**
     * Adds custom {@link Instrument} enum values.
     */
    @Override
    public void run() {
        MappingResolver remapper = FabricLoader.getInstance().getMappingResolver();
        String instrumentClassName = remapper.mapClassName("intermediary", "net.minecraft.class_2766");
        String soundEventInternalClassName = 'L' + remapper.mapClassName("intermediary", "net.minecraft.class_3414") + ';';

        for (CustomInstrument instrument : CustomInstrument.values()) {
            ClassTinkerers.enumBuilder(instrumentClassName, String.class, soundEventInternalClassName)
                    .addEnum(instrument.getEnumName(), () -> new Object[]{instrument.getInstrumentName(), instrument.getInstrumentSound()})
                    .build();
        }
    }
}
