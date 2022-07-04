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

import java.util.Set;

@SuppressWarnings("unused")
@Mixin(ParrotEntity.class)
abstract class ParrotEntityMixin extends TameableEntityMixin {

    @Shadow
    @Final
    private static Item COOKIE;
    @Shadow
    @Final
    private static Set<Item> TAMING_INGREDIENTS;

    protected ParrotEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * If the owner of a parrot interacts with the parrot while sneaking,
     * it will toggle whether the parrot follows the owner or not.
     */
    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    private void catsPlus$setFollowing(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (isTamed() && isOwner(player)) {
            ItemStack itemStack = player.getStackInHand(hand);
            if (!itemStack.isOf(COOKIE) && !TAMING_INGREDIENTS.contains(itemStack.getItem())) {
                boolean isClient = getWorld().isClient();
                if (!isClient) {
                    catsPlus$toggleFollowing();
                }
                cir.setReturnValue(ActionResult.success(isClient));
            }
        }
    }
}
