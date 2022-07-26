package xyz.foxkin.catsplus.mixin.commonloader.commonside.entitypickup;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import xyz.foxkin.catsplus.commonside.access.entitypickup.PlayerEntityAccess;

@Mixin(PlayerInventory.class)
abstract class PlayerInventoryMixin {

    @Shadow
    @Final
    public PlayerEntity player;
    @Shadow
    public int selectedSlot;

    /**
     * If the player is holding an entity, the player's selected slot will be treated as occupied.
     */
    @Redirect(method = "getEmptySlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/DefaultedList;get(I)Ljava/lang/Object;"))
    private Object catsPlus$preventSelectedSlotPickupIfHoldingEntity(DefaultedList<ItemStack> mainInventory, int slotIndex) {
        if (slotIndex == selectedSlot) {
            PlayerEntityAccess playerAccess = (PlayerEntityAccess) player;
            if (playerAccess.catsPlus$getHeldEntity().isPresent()) {
                return new ItemStack(Items.STONE);
            }
        }
        return mainInventory.get(slotIndex);
    }
}
