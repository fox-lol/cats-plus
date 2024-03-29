package xyz.foxkin.catsplus.mixin.commonloader.client.entitypickup;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import xyz.foxkin.catsplus.client.access.render.AnimatableContainer;
import xyz.foxkin.catsplus.client.animatable.CatsPlusAnimatable;
import xyz.foxkin.catsplus.client.animatable.entity.player.ThirdPersonPlayerArms;
import xyz.foxkin.catsplus.client.init.ModGeoRenderers;
import xyz.foxkin.catsplus.client.render.CatsPlusGeoRenderer;
import xyz.foxkin.catsplus.client.util.GeckoUtil;
import xyz.foxkin.catsplus.commonside.CatsPlus;
import xyz.foxkin.catsplus.commonside.access.entitypickup.PlayerEntityAccess;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(LivingEntityRenderer.class)
abstract class LivingEntityRendererMixin<T extends LivingEntity> extends EntityRenderer<T> {

    @SuppressWarnings("unused")
    protected LivingEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    /**
     * Renders the third-person perspective of the held entity if one is being held by the player.
     */
    @SuppressWarnings("unchecked")
    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V"))
    protected void catsPlus$renderThirdPersonHeldEntity(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (entity instanceof PlayerEntityAccess playerAccess) {
            playerAccess.catsPlus$getHeldEntity().ifPresent(heldEntity -> {
                AnimatableContainer<ThirdPersonPlayerArms> animatableContainer = (AnimatableContainer<ThirdPersonPlayerArms>) entity;
                ThirdPersonPlayerArms playerArms = animatableContainer.catsPlus$getAnimatable();
                matrices.push();
                matrices.translate(0, 1.5, 0);
                matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));
                if (playerArms.getEntity().isInSneakingPose()) {
                    matrices.translate(0, -0.08, 0.6);
                    matrices.multiply(Vec3f.POSITIVE_X.getRadialQuaternion(-0.4F));
                }

                boolean heldEntityVisible;
                if (heldEntity instanceof LivingEntity livingEntity) {
                    heldEntityVisible = !livingEntity.hasStatusEffect(StatusEffects.INVISIBILITY);
                } else {
                    heldEntityVisible = true;
                }
                if (heldEntityVisible) {
                    catsPlus$renderThirdPersonHeldEntity(playerArms, heldEntity, matrices, vertexConsumers, light);
                }

                matrices.pop();
            });
        }
    }

    /**
     * Renders the third-person perspective of the held entity.
     *
     * @param holder          The animatable representing the player's arms.
     * @param heldEntity      The held entity.
     * @param matrices        The matrix transformations stack.
     * @param vertexConsumers The vertex consumer provider.
     * @param light           The light level.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Unique
    private void catsPlus$renderThirdPersonHeldEntity(ThirdPersonPlayerArms holder, Entity heldEntity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        AtomicBoolean renderedHeldEntity = new AtomicBoolean(false);
        if (heldEntity instanceof AnimatableContainer<?> container) {
            CatsPlusAnimatable animatable = container.catsPlus$getAnimatable();
            Optional<? extends CatsPlusGeoRenderer> heldEntityRendererOptional = ModGeoRenderers.getRenderer(animatable.getClass());
            heldEntityRendererOptional.ifPresentOrElse(heldEntityRenderer -> {
                var holderRenderer = ModGeoRenderers.getRenderer(ThirdPersonPlayerArms.class).orElseThrow();
                GeoModel playerArmsModel = holderRenderer.getGeoModel(holder);
                playerArmsModel.getBone("entity_placeholder").ifPresentOrElse(entityPlaceholder -> {
                    GeckoUtil.applyBoneTransformations(entityPlaceholder, matrices);
                    heldEntityRenderer.render(animatable, matrices, vertexConsumers, light);
                    renderedHeldEntity.set(true);
                }, () -> CatsPlus.LOGGER.error("Could not find bone \"entity_placeholder\" in model!"));
            }, () -> CatsPlus.LOGGER.error("Could not find renderer for animatable " + animatable.getClass().getName()));
        }
        if (!renderedHeldEntity.get()) {
            matrices.translate(0, 0.2, -0.5);
            matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90));
            EntityRenderer<? super Entity> entityRenderer = dispatcher.getRenderer(heldEntity);
            entityRenderer.render(heldEntity, 0, 0, matrices, vertexConsumers, light);
        }
        matrices.pop();
    }
}
