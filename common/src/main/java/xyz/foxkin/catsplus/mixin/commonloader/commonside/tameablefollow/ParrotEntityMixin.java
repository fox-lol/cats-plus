package xyz.foxkin.catsplus.mixin.commonloader.commonside.tameablefollow;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.foxkin.catsplus.commonside.init.ModTags;

import java.util.Set;

@Mixin(ParrotEntity.class)
abstract class ParrotEntityMixin extends TameableEntityMixin {

    @Shadow
    @Final
    private static Item COOKIE;
    @Shadow
    @Final
    private static Set<Item> TAMING_INGREDIENTS;

    @SuppressWarnings("unused")
    protected ParrotEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * If the owner of a cat interacts with the cat while holding an item
     * tagged with {@link ModTags#TOGGLE_PARROT_FOLLOWING}, it will toggle
     * whether the cat follows the owner or not.
     */
    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    private void catsPlus$ownerToggleFollowing(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (isTamed() && isOwner(player)) {
            ItemStack itemStack = player.getStackInHand(hand);
            if (!itemStack.isOf(COOKIE) && !TAMING_INGREDIENTS.contains(itemStack.getItem()) && itemStack.isIn(ModTags.TOGGLE_PARROT_FOLLOWING)) {
                boolean isClient = getWorld().isClient();
                if (!isClient) {
                    catsPlus$toggleFollowing();
                }
                cir.setReturnValue(ActionResult.SUCCESS);
            }
        }
    }
}
