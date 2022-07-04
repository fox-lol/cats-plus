package xyz.foxkin.catsplus.mixin.commonloader.commonside.catsitonblock;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.*;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.enums.BedPart;
import net.minecraft.entity.ai.goal.CatSitOnBlockGoal;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.apache.commons.lang3.function.TriFunction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.foxkin.catsplus.commonside.init.ModTags;

import java.util.List;

@Mixin(CatSitOnBlockGoal.class)
abstract class CatSitOnBlockGoalMixin {

    @Shadow
    @Final
    private CatEntity cat;

    /**
     * Extra checks to be performed before a {@link CatEntity} can sit on a block tagged with {@link ModTags#CAT_SIT_ON}.
     * These take in the {@code WorldView} the cat is in, the {@code BlockPos} of the block being checked,
     * and the {@code BlockState} of the {@link Block} being checked, and return {@link ActionResult#SUCCESS}
     * if the cat can sit on the block, {@link ActionResult#FAIL} if it can't, and {@link ActionResult#PASS} if
     * the next check should be run.
     */
    @Unique
    private final List<TriFunction<WorldView, BlockPos, BlockState, ActionResult>> CATS_PLUS$EXTRA_CHECKS = ImmutableList.of(
            /*
            Cats will only sit on chests that are not currently being looked at by a player.
            If a cat is already sat on a chest and a player opens the chest, the cat will remain sitting.
             */
            (world, pos, blockState) -> {
                if (blockState.getBlock() instanceof AbstractChestBlock<?>) {
                    boolean catIsAlreadyOnChest = cat.getBlockPos().equals(pos);
                    if (catIsAlreadyOnChest || ChestBlockEntity.getPlayersLookingInChestCount(world, pos) < 1) {
                        return ActionResult.SUCCESS;
                    } else {
                        return ActionResult.FAIL;
                    }
                } else {
                    return ActionResult.PASS;
                }
            },
            /*
            Cats will only sit on furnace-like blocks that are lit.
             */
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
            /*
            Cats will not sit on the head part of beds.
             */
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

    /**
     * Cats can sit on blocks tagged with {@link ModTags#CAT_SIT_ON}. Replaces the hardcoded block checks.
     */
    @Inject(method = "isTargetPos", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldView;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"), cancellable = true)
    private void catsPlus$tagBasedBlockSit(WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        BlockState blockState = world.getBlockState(pos);
        if (blockState.isIn(ModTags.CAT_SIT_ON)) {
            for (TriFunction<WorldView, BlockPos, BlockState, ActionResult> check : CATS_PLUS$EXTRA_CHECKS) {
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
