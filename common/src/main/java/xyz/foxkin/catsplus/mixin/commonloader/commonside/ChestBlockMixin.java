package xyz.foxkin.catsplus.mixin.commonloader.commonside;

import net.minecraft.block.ChestBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.foxkin.catsplus.commonside.CatsPlus;

@Mixin(ChestBlock.class)
abstract class ChestBlockMixin {

    /**
     * Allows chests to be opened by a player even when a cat is sitting on it, if enabled in config.
     */
    @Inject(method = "hasCatOnTop", at = @At("HEAD"), cancellable = true)
    private static void catsPlus$allowCatSitOnChestOpen(WorldAccess world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (CatsPlus.getConfig().isCatSittingOnChestAllowsOpening()) {
            cir.setReturnValue(false);
        }
    }
}
