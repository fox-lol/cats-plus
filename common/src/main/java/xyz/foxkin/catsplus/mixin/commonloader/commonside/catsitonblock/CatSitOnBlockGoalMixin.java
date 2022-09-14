package xyz.foxkin.catsplus.mixin.commonloader.commonside.catsitonblock;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.*;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.enums.BedPart;
import net.minecraft.entity.ai.goal.CatSitOnBlockGoal;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.TagKey;
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
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.foxkin.catsplus.commonside.access.catsitonblock.CatEntityAccess;
import xyz.foxkin.catsplus.commonside.access.catsitonblock.CatSitOnBlockGoalAccess;
import xyz.foxkin.catsplus.commonside.init.ModTags;

import java.util.List;

@Mixin(CatSitOnBlockGoal.class)
public abstract class CatSitOnBlockGoalMixin implements CatSitOnBlockGoalAccess {

    @Shadow
    @Final
    private CatEntity cat;

    /**
     * Extra checks to be performed before a {@link CatEntity} can sit or sleep on a block tagged with {@link CatSitOnBlockGoalAccess#catsPlus$getBlockTag()}.
     * These take in the {@code WorldView} the cat is in, the {@code BlockPos} of the block being checked,
     * and the {@code BlockState} of the {@link Block} being checked, and return {@link ActionResult#SUCCESS}
     * if the cat can sit or sleep on the block, {@link ActionResult#FAIL} if it can't, and {@link ActionResult#PASS} if
     * the next check should be run.
     */
    @Unique
    private final List<TriFunction<WorldView, BlockPos, BlockState, ActionResult>> CATS_PLUS$EXTRA_CHECKS = ImmutableList.of(
            /*
            Cats will only sit or sleep on chests that are not currently being looked at by a player.
            If a cat is already sat on a chest and a player opens the chest, the cat will remain sitting or sleeping.
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
            Cats will only sit or sleep on furnace-like blocks that are lit.
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
            Cats will not sit or sleep on the head part of beds.
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
     * Adds extra conditions that must be satisfied before a cat can sit or sleep on a block.
     */
    @SuppressWarnings("unused")
    @ModifyReturnValue(method = "canStart", at = @At("RETURN"))
    private boolean catsPlus$addExtraStartConditions(boolean canStart) {
        CatEntityAccess catAccess = (CatEntityAccess) cat;
        return canStart && catAccess.catsPlus$canSitOrSleep() && catsPlus$extraStartCondition();
    }

    /**
     * Lets the pose a cat is set in when it gets on top of a valid block be determined by subclasses.
     */
    @Redirect(method = {"start", "stop", "tick"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/CatEntity;setInSittingPose(Z)V"))
    private void catsPlus$changeSetPoseMethod(CatEntity cat, boolean inSittingPose) {
        catsPlus$setInPose(inSittingPose);
    }

    /**
     * Sets the cooldown for sitting or sleeping when the goal is stopped.
     */
    @Inject(method = "stop", at = @At("HEAD"))
    private void catsPlus$setSitOrSleepCooldown(CallbackInfo ci) {
        CatEntityAccess access = (CatEntityAccess) cat;
        access.catsPlus$setSitOrSleepCooldown(1200);
    }

    /**
     * Cats can sit or sleep on blocks tagged with {@link CatSitOnBlockGoalAccess#catsPlus$getBlockTag()}. Replaces the hardcoded block checks.
     */
    @Inject(method = "isTargetPos", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldView;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"), cancellable = true)
    private void catsPlus$tagBasedBlockSit(WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        BlockState blockState = world.getBlockState(pos);
        if (blockState.isIn(catsPlus$getBlockTag())) {
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

    @Unique
    @Override
    public boolean catsPlus$extraStartCondition() {
        return !cat.isInSleepingPose();
    }

    @Unique
    @Override
    public TagKey<Block> catsPlus$getBlockTag() {
        return ModTags.CAT_SIT_ON;
    }

    @Unique
    @Override
    public void catsPlus$setInPose(boolean inPose) {
        cat.setInSittingPose(inPose);
    }
}
