package xyz.foxkin.catsplus.mixin.commonloader.commonside;

import net.minecraft.block.BlockState;
import net.minecraft.block.enums.Instrument;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.foxkin.catsplus.commonside.block.InstrumentMaterial;

@Mixin(Instrument.class)
public class InstrumentMixin {

    /**
     * If the {@link BlockState}'s material is an instance of {@link InstrumentMaterial}, the corresponding instrument will be played.
     */
    @Inject(method = "fromBlockState", at = @At("HEAD"), cancellable = true)
    private static void customMaterialNoteBlockInstrument(BlockState state, CallbackInfoReturnable<Instrument> cir) {
        if (state.getMaterial() instanceof InstrumentMaterial instrumentMaterial) {
            cir.setReturnValue(instrumentMaterial.getInstrument());
        }
    }
}
