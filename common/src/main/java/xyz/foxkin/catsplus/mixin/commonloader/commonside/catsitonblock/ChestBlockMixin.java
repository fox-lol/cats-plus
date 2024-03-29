package xyz.foxkin.catsplus.mixin.commonloader.commonside.catsitonblock;

import net.minecraft.block.ChestBlock;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.foxkin.catsplus.commonside.CatsPlus;

@Mixin(ChestBlock.class)
abstract class ChestBlockMixin {

    /**
     * Allows chests to be opened by a player even when a cat is sitting on it, if enabled in config.
     */
    @Inject(method = "hasOcelotOnTop", at = @At("HEAD"), cancellable = true)
    private static void catsPlus$allowCatSitOnChestOpen(WorldAccess world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (CatsPlus.getConfig().isCatSittingOnChestAllowsOpening()) {
            cir.setReturnValue(false);
        }
    }

    /**
     * A cat of top of a chest in the sleeping pose will prevent the chest from being opened.
     */
    @Redirect(method = "hasOcelotOnTop", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/CatEntity;isInSittingPose()Z"))
    private static boolean catsPlus$preventChestOpenCatSleeping(CatEntity cat) {
        return cat.isInSittingPose() || cat.isInSleepingPose();
    }
}
