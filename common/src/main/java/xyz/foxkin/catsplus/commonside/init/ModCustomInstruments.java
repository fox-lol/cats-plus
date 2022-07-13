package xyz.foxkin.catsplus.commonside.init;

import io.github.shaksternano.noteblocklib.commonside.CustomInstrument;
import net.minecraft.sound.SoundEvents;
import xyz.foxkin.catsplus.commonside.CatsPlus;

public class ModCustomInstruments {

    public static final CustomInstrument CAT = new CustomInstrument(CatsPlus.MOD_ID, "cat", () -> SoundEvents.ENTITY_CAT_AMBIENT);
}
