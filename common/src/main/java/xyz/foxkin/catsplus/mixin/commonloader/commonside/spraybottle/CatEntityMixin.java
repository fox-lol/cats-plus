package xyz.foxkin.catsplus.mixin.commonloader.commonside.spraybottle;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.foxkin.catsplus.commonside.access.spraybottle.CatEntityAccess;
import xyz.foxkin.catsplus.commonside.entity.ai.goal.SprayDangerGoal;
import xyz.foxkin.catsplus.commonside.init.ModItems;

@Mixin(CatEntity.class)
abstract class CatEntityMixin extends TameableEntity implements CatEntityAccess {

    private int catsPlus$fleeTicks;

    protected CatEntityMixin(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void catsPlus$decrementFleeTicks(CallbackInfo ci) {
        if (catsPlus$fleeTicks > 0) {
            setSitting(false);
            catsPlus$fleeTicks--;
        }
    }

    @Inject(method = "initGoals", at = @At("HEAD"))
    private void catsPlus$addSprayDangerGoal(CallbackInfo ci) {
        goalSelector.add(1, new SprayDangerGoal(this, 2));
    }

    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    private void catsPlus$allowSprayBottleEffects(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (player.getStackInHand(hand).isOf(ModItems.SPRAY_BOTTLE.get())) {
            cir.setReturnValue(ActionResult.PASS);
        }
    }

    @Override
    public int catsPlus$getFleeTicks() {
        return catsPlus$fleeTicks;
    }

    @Override
    public void catsPlus$setFleeTicks(int ticks) {
        catsPlus$fleeTicks = ticks;
    }
}
