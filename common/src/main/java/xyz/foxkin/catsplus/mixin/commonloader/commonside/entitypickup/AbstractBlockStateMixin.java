package xyz.foxkin.catsplus.mixin.commonloader.commonside.entitypickup;

import net.minecraft.block.AbstractBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.foxkin.catsplus.commonside.access.entitypickup.PlayerEntityAccess;

@Mixin(AbstractBlock.AbstractBlockState.class)
abstract class AbstractBlockStateMixin {

    /**
     * When a player is holding an entity and interacts with a block,
     * the entity is dropped next to the face of the block that was interacted with.
     */
    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    private void catsPlus$dropHeldEntity(World world, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        PlayerEntityAccess playerAccess = (PlayerEntityAccess) player;
        playerAccess.catsPlus$getHeldEntity().ifPresent(entity -> {
            Vec3d position = hit.getPos();

            double x = position.getX();
            double y = position.getY();
            double z = position.getZ();

            double halfEntityLength = entity.getBoundingBox().getXLength() / 2;
            double entityHeight = entity.getBoundingBox().getYLength();

            switch (hit.getSide()) {
                case NORTH -> z -= halfEntityLength;
                case SOUTH -> z += halfEntityLength;
                case EAST -> x += halfEntityLength;
                case WEST -> x -= halfEntityLength;
                case DOWN -> y -= entityHeight;
            }

            playerAccess.catsPlus$dropHeldEntity(x, y, z);
            cir.setReturnValue(ActionResult.SUCCESS);
        });
    }
}
