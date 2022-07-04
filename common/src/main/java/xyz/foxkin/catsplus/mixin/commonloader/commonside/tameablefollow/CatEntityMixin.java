package xyz.foxkin.catsplus.mixin.commonloader.commonside.tameablefollow;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@SuppressWarnings("unused")
@Mixin(CatEntity.class)
abstract class CatEntityMixin extends TameableEntityMixin {

    protected CatEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * If the owner of a cat interacts with the cat while sneaking,
     * it will toggle whether the cat follows the owner or not.
     */
    @WrapWithCondition(method = "interactMob", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/CatEntity;setSitting(Z)V", ordinal = 0))
    private boolean catsPlus$setFollowing(CatEntity thisCat, boolean isSitting, PlayerEntity player) {
        if (player.isSneaking()) {
            catsPlus$toggleFollowing();
            return false;
        } else {
            return true;
        }
    }
}
