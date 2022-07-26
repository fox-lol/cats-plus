package xyz.foxkin.catsplus.mixin.commonloader.client.entitypickup;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import xyz.foxkin.catsplus.client.access.render.AnimatableContainer;
import xyz.foxkin.catsplus.client.animatable.CatsPlusAnimatable;
import xyz.foxkin.catsplus.client.animatable.player.FirstPersonPlayerArms;
import xyz.foxkin.catsplus.client.animatable.player.PlayerArms;
import xyz.foxkin.catsplus.client.init.ModGeoRenderers;
import xyz.foxkin.catsplus.client.render.CatsPlusGeoRenderer;
import xyz.foxkin.catsplus.commonside.CatsPlus;
import xyz.foxkin.catsplus.commonside.access.entitypickup.PlayerEntityAccess;
import xyz.foxkin.catsplus.commonside.util.GeckoUtil;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

// TODO: Replace with events on Forge.
@Mixin(HeldItemRenderer.class)
abstract class HeldItemRendererMixin {

    @Shadow
    @Final
    private EntityRenderDispatcher entityRenderDispatcher;

    /**
     * Renders the first-person perspective of the players arms in a holding position along with the held entity.
     */
    @Inject(method = "renderFirstPersonItem", at = @At(value = "FIELD", target = "Lnet/minecraft/util/Hand;MAIN_HAND:Lnet/minecraft/util/Hand;"), cancellable = true)
    private void catsPlus$renderFirstPersonHoldingEntity(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        PlayerEntityAccess playerAccess = (PlayerEntityAccess) player;
        playerAccess.catsPlus$getHeldEntity().ifPresent(heldEntity -> {
            if (hand == Hand.MAIN_HAND) {
                PlayerArms playerArms = FirstPersonPlayerArms.getInstance();
                catsPlus$renderFirstPersonHoldingArms(playerArms, matrices, vertexConsumers, light);
                catsPlus$renderFirstPersonHeldEntity(playerArms, heldEntity, matrices, vertexConsumers, light);
            }
            ci.cancel();
        });
    }

    /**
     * Renders the first-person perspective of the players arms in a holding position.
     *
     * @param playerArms      The animatable representing the player's arms.
     * @param matrices        The matrix transformations stack.
     * @param vertexConsumers The vertex consumer provider.
     * @param light           The light level.
     */
    private void catsPlus$renderFirstPersonHoldingArms(PlayerArms playerArms, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        matrices.translate(0, -2, -1);
        CatsPlusGeoRenderer<PlayerArms> renderer = ModGeoRenderers.getRenderer(PlayerArms.class).orElseThrow();
        renderer.render(playerArms, matrices, vertexConsumers, light);
        matrices.pop();
    }

    /**
     * Renders the first-person perspective of the held entity.
     *
     * @param holder          The animatable representing the player's arms.
     * @param heldEntity      The held entity.
     * @param matrices        The matrix transformations stack.
     * @param vertexConsumers The vertex consumer provider.
     * @param light           The light level.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void catsPlus$renderFirstPersonHeldEntity(PlayerArms holder, Entity heldEntity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();

        AtomicBoolean renderedHeldEntity = new AtomicBoolean(false);
        if (heldEntity instanceof AnimatableContainer<?> container) {
            CatsPlusAnimatable animatable = container.catsPlus$getAnimatable();
            Optional<? extends CatsPlusGeoRenderer> rendererOptional = ModGeoRenderers.getRenderer(animatable.getClass());
            rendererOptional.ifPresentOrElse(renderer -> {
                CatsPlusGeoRenderer<PlayerArms> holderRenderer = ModGeoRenderers.getRenderer(PlayerArms.class).orElseThrow();
                GeoModelProvider<PlayerArms> holderModelProvider = holderRenderer.getGeoModelProvider();
                GeoModel model = holderModelProvider.getModel(holderRenderer.getGeoModelProvider().getModelResource(holder));
                model.getBone("entity_placeholder").ifPresentOrElse(entityPlaceholder -> {
                    matrices.translate(0, -0.4, -0.81);
                    GeckoUtil.applyBoneTransformations(entityPlaceholder, matrices);
                    matrices.translate(0, -0.05, -0.2);
                    entityPlaceholder.setHidden(true);
                    renderer.render(animatable, matrices, vertexConsumers, light);
                    renderedHeldEntity.set(true);
                }, () -> CatsPlus.LOGGER.error("Could not find bone \"entity_placeholder\" in model!"));
            }, () -> CatsPlus.LOGGER.error("Could not find renderer for animatable " + animatable.getClass().getName()));
        }
        if (!renderedHeldEntity.get()) {
            matrices.translate(0, -0.5, -1);
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90));
            EntityRenderer<? super Entity> entityRenderer = entityRenderDispatcher.getRenderer(heldEntity);
            entityRenderer.render(heldEntity, 0, 0, matrices, vertexConsumers, light);
        }
        matrices.pop();
    }
}
