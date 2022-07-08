package xyz.foxkin.catsplus.mixin.commonloader.client.entitypickup;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.foxkin.catsplus.commonside.access.entitypickup.PlayerEntityAccess;

// TODO: Replace with events on Forge.
@Mixin(HeldItemRenderer.class)
abstract class HeldItemRendererMixin {

    @Shadow
    @Final
    private MinecraftClient client;
    @Shadow
    @Final
    private EntityRenderDispatcher entityRenderDispatcher;

    @Shadow
    protected abstract void renderArmHoldingItem(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float equipProgress, float swingProgress, Arm arm);

    /**
     * Renders the players arms in a holding position.
     */
    @SuppressWarnings("ConstantConditions")
    @Inject(method = "renderArmHoldingItem", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;entityRenderDispatcher:Lnet/minecraft/client/render/entity/EntityRenderDispatcher;"))
    private void catsPlus$renderHoldingHands(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float equipProgress, float swingProgress, Arm arm, CallbackInfo ci) {
        PlayerEntity player = client.player;
        PlayerEntityAccess playerAccess = (PlayerEntityAccess) player;
        if (playerAccess.catsPlus$isHoldingEntity()) {
            boolean mainArm = arm == Arm.RIGHT;
            float armSideMultiplier = mainArm ? 1 : -1;

            double xTranslation = 0.1;
            double yTranslation = 0;
            double zTranslation = 0.2;

            matrices.translate(xTranslation * armSideMultiplier, yTranslation, zTranslation);

            int xDegrees = -30;
            int yDegrees = 0;
            int zDegrees = -20;

            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(xDegrees));
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(yDegrees * armSideMultiplier));
            matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(zDegrees * armSideMultiplier));
        }
    }

    /**
     * Renders the players arms in a holding position along with the held entity.
     */
    @Inject(method = "renderFirstPersonItem", at = @At(value = "FIELD", target = "Lnet/minecraft/util/Hand;MAIN_HAND:Lnet/minecraft/util/Hand;"), cancellable = true)
    private void catsPlus$renderHoldingEntity(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        PlayerEntityAccess playerAccess = (PlayerEntityAccess) player;
        if (playerAccess.catsPlus$isHoldingEntity()) {
            boolean mainArm = hand == Hand.MAIN_HAND;
            Arm arm = mainArm ? player.getMainArm() : player.getMainArm().getOpposite();

            matrices.push();
            renderArmHoldingItem(matrices, vertexConsumers, light, equipProgress, swingProgress, arm);
            matrices.pop();

            EntityType.getEntityFromNbt(playerAccess.catsPlus$getHeldEntity(), player.getWorld()).ifPresent(entity -> {
                EntityRenderer<? super Entity> renderer = entityRenderDispatcher.getRenderer(entity);
                matrices.push();

                double xTranslation = 0;
                double yTranslation = -0.05;
                double zTranslation = -1.2;

                matrices.translate(xTranslation, yTranslation, zTranslation);

                int xDegrees = 0;
                int yDegrees = 90;
                int zDegrees = 180;

                matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(xDegrees));
                matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(yDegrees));
                matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(zDegrees));

                renderer.render(entity, 0, 0, matrices, vertexConsumers, light);
                matrices.pop();
            });

            ci.cancel();
        }
    }
}
