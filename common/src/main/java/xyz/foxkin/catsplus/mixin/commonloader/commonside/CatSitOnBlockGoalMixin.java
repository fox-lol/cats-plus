package xyz.foxkin.catsplus.mixin.commonloader.commonside;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.*;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.enums.BedPart;
import net.minecraft.entity.ai.goal.CatSitOnBlockGoal;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.apache.commons.lang3.function.TriFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.foxkin.catsplus.commonside.registry.ModTags;

import java.util.List;

@Mixin(CatSitOnBlockGoal.class)
public class CatSitOnBlockGoalMixin {

    private static final List<TriFunction<WorldView, BlockPos, BlockState, ActionResult>> EXTRA_CHECKS = ImmutableList.of(
            (world, pos, blockState) -> {
                if (blockState.getBlock() instanceof AbstractChestBlock<?>) {
                    if (ChestBlockEntity.getPlayersLookingInChestCount(world, pos) < 1) {
                        return ActionResult.SUCCESS;
                    } else {
                        return ActionResult.FAIL;
                    }
                } else {
                    return ActionResult.PASS;
                }
            },
            (world, pos, blockState) -> {
                if (blockState.getBlock() instanceof AbstractFurnaceBlock) {
                    if (blockState.get(FurnaceBlock.LIT)) {
                        return ActionResult.SUCCESS;
                    } else {
                        return ActionResult.FAIL;
                    }
                } else {
                    return ActionResult.PASS;
                }
            },
            (world, pos, blockState) -> {
                if (blockState.isIn(BlockTags.BEDS)) {
                    if (blockState.getOrEmpty(BedBlock.PART).map(part -> part != BedPart.HEAD).orElse(true)) {
                        return ActionResult.SUCCESS;
                    } else {
                        return ActionResult.FAIL;
                    }
                } else {
                    return ActionResult.PASS;
                }
            }
    );

    @Inject(method = "isTargetPos", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldView;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"), cancellable = true)
    private void tagBasedBlockSit(WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        BlockState blockState = world.getBlockState(pos);
        if (blockState.isIn(ModTags.CAT_SIT_ON)) {
            for (TriFunction<WorldView, BlockPos, BlockState, ActionResult> check : EXTRA_CHECKS) {
                ActionResult result = check.apply(world, pos, blockState);
                if (result == ActionResult.SUCCESS) {
                    cir.setReturnValue(true);
                    return;
                } else if (result == ActionResult.FAIL) {
                    cir.setReturnValue(false);
                    return;
                }
            }

            cir.setReturnValue(true);
        } else {
            cir.setReturnValue(false);
        }
    }
}
