package xyz.foxkin.catsplus.mixin.commonloader.client.entitypickup;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.util.RenderUtils;
import xyz.foxkin.catsplus.client.access.render.AnimatableContainer;
import xyz.foxkin.catsplus.client.animatable.CatsPlusAnimatable;
import xyz.foxkin.catsplus.client.animatable.player.PlayerArms;
import xyz.foxkin.catsplus.client.animatable.player.ThirdPersonPlayerArms;
import xyz.foxkin.catsplus.client.init.ModGeoRenderers;
import xyz.foxkin.catsplus.client.render.CatsPlusGeoRenderer;
import xyz.foxkin.catsplus.commonside.CatsPlus;
import xyz.foxkin.catsplus.commonside.access.entitypickup.PlayerEntityAccess;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(LivingEntityRenderer.class)
abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements FeatureRendererContext<T, M> {

    protected LivingEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @SuppressWarnings("unchecked")
    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V"))
    protected void catsPlus$renderHoldingArmsAndEntity(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (entity instanceof PlayerEntityAccess playerAccess) {
            if (playerAccess.catsPlus$isHoldingEntity()) {
                playerAccess.catsPlus$getHeldEntity().ifPresent(heldEntity -> {
                    AnimatableContainer<ThirdPersonPlayerArms> animatableContainer = (AnimatableContainer<ThirdPersonPlayerArms>) entity;
                    PlayerArms playerArms = animatableContainer.catsPlus$getAnimatable();

                    catsPlus$renderHoldingArms(playerArms, matrices, vertexConsumers, light);
                    catsPlus$renderHeldEntity(playerArms, heldEntity, matrices, vertexConsumers, light);
                });
            }
        }
    }

    private void catsPlus$renderHoldingArms(PlayerArms playerArms, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        CatsPlusGeoRenderer<PlayerArms> renderer = ModGeoRenderers.getRenderer(PlayerArms.class).orElseThrow();
        matrices.push();
        matrices.translate(0, 1.764, -0.1);
        matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));
        if (playerArms.isInSneakingPose()) {
            matrices.translate(0, -0.1, 0.7);
            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-23));
        }
        renderer.render(playerArms, matrices, vertexConsumers, light);
        matrices.pop();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void catsPlus$renderHeldEntity(PlayerArms holder, Entity heldEntity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();

        AtomicBoolean renderedHeldEntity = new AtomicBoolean(false);
        if (heldEntity instanceof AnimatableContainer<?> container) {
            CatsPlusAnimatable animatable = container.catsPlus$getAnimatable();

            Optional<? extends CatsPlusGeoRenderer> rendererOptional = ModGeoRenderers.getRenderer(animatable.getClass());
            rendererOptional.ifPresentOrElse(renderer -> {
                CatsPlusGeoRenderer<PlayerArms> holderRenderer = ModGeoRenderers.getRenderer(PlayerArms.class).orElseThrow();
                GeoModelProvider<PlayerArms> holderModelProvider = holderRenderer.getGeoModelProvider();

                GeoModel model = holderModelProvider.getModel(holderRenderer.getGeoModelProvider().getModelResource(holder));
                model.getBone("root").ifPresentOrElse(root -> model.getBone("entity_placeholder").ifPresentOrElse(entityPlaceholder -> {
                    matrices.translate(0, 0.2, -0.1);
                    matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));
                    if (holder.isInSneakingPose()) {
                        matrices.translate(0, -0.22, 0.1);
                        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-23));
                    }
                    RenderUtils.translate(root, matrices);
                    RenderUtils.translate(entityPlaceholder, matrices);
                    RenderUtils.rotate(root, matrices);
                    RenderUtils.rotate(entityPlaceholder, matrices);
                    entityPlaceholder.setHidden(true);
                    renderer.render(animatable, matrices, vertexConsumers, light);
                    renderedHeldEntity.set(true);
                }, () -> CatsPlus.LOGGER.error("Could not find bone \"entity_placeholder\" in model!")), () -> CatsPlus.LOGGER.error("Could not find bone \"root\" in model!"));
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
