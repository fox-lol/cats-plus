package xyz.foxkin.catsplus.commonside.block;

import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.NoteBlock;
import net.minecraft.block.enums.Instrument;
import net.minecraft.block.piston.PistonBehavior;
import xyz.foxkin.catsplus.commonside.block.enums.CustomInstrument;

/**
 * {@link Block}s with materials of this class will have the {@link Instrument} played
 * when a {@link NoteBlock} above the {@code Block} is played.
 */
public class InstrumentMaterial extends Material {

    private final Instrument INSTRUMENT;

    public InstrumentMaterial(MapColor color, Instrument instrument) {
        super(color, false, true, true, true, false, false, PistonBehavior.NORMAL);
        INSTRUMENT = instrument;
    }

    public InstrumentMaterial(MapColor color, CustomInstrument customInstrument) {
        this(color, customInstrument.getInstrument());
    }

    public Instrument getInstrument() {
        return INSTRUMENT;
    }
}
