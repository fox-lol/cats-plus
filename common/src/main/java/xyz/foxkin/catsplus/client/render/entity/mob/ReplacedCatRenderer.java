package xyz.foxkin.catsplus.client.render.entity.mob;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoCube;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.util.RenderUtils;
import xyz.foxkin.catsplus.client.animatable.entity.mob.ReplacedCatAnimatable;
import xyz.foxkin.catsplus.client.model.entity.mob.ReplacedCatModel;
import xyz.foxkin.catsplus.client.render.CatsPlusGeoRenderer;
import xyz.foxkin.catsplus.client.util.GeckoUtil;
import xyz.foxkin.catsplus.mixin.commonloader.client.accessor.CatCollarFeatureRendererAccessor;

@Environment(EnvType.CLIENT)
public class ReplacedCatRenderer extends CatsPlusGeoRenderer<ReplacedCatAnimatable, ReplacedCatModel> {

    public ReplacedCatRenderer(ReplacedCatModel modelProvider) {
        super(modelProvider, ReplacedCatAnimatable.class);
    }

    @Override
    public void render(ReplacedCatAnimatable animatable, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        float modelScale = 0.8F;
        matrices.scale(modelScale, modelScale, modelScale);
        super.render(animatable, matrices, vertexConsumers, light);
        if (animatable.getEntity().isTamed()) {
            renderCollar(animatable, matrices, vertexConsumers, light);
        }
        matrices.pop();
    }

    /**
     * Renders the collar of the cat
     *
     * @param animatable      The animatable representing the cat.
     * @param matrices        The matrix transformations stack.
     * @param vertexConsumers The vertex consumer provider.
     * @param light           The light level.
     */
    private void renderCollar(ReplacedCatAnimatable animatable, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        GeoModel model = getGeoModel(animatable);
        RenderLayer collarLayer = RenderLayer.getEntityCutoutNoCull(CatCollarFeatureRendererAccessor.catsPlus$getSkin());
        float[] collarColor = animatable.getEntity().getCollarColor().getColorComponents();
        render(model, animatable, 0, collarLayer, matrices, vertexConsumers, null, light, OverlayTexture.DEFAULT_UV, collarColor[0], collarColor[1], collarColor[2], 1);
    }

    @Override
    public void render(GeoModel model, ReplacedCatAnimatable animatable, float tickDelta, RenderLayer renderLayer, MatrixStack matrices, VertexConsumerProvider vertexConsumers, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        renderEarly(animatable, matrices, tickDelta, vertexConsumers, vertices, light, overlay, red, green, blue, alpha);

        if (vertexConsumers != null) {
            vertices = vertexConsumers.getBuffer(renderLayer);
        }
        renderLate(animatable, matrices, tickDelta, vertexConsumers, vertices, light, overlay, red, green, blue, alpha);
        boolean isBaby = animatable.getEntity().isBaby();

        matrices.push();
        if (isBaby) {
            matrices.translate(0, -0.85, 0);
        }
        for (GeoBone group : model.topLevelBones) {
            renderRecursively(group, matrices, vertices, light, overlay, red, green, blue, alpha, isBaby);
        }
        matrices.pop();
    }

    /**
     * Renders the bones of the model.
     *
     * @param bone     The parent of all the bones to render.
     * @param matrices The matrix transformations stack.
     * @param vertices The vertex consumer.
     * @param light    The light level.
     * @param overlay  The overlay.
     * @param red      The red tint level.
     * @param green    The green tint level.
     * @param blue     The blue tint level.
     * @param alpha    The alpha level.
     * @param isBaby   Whether the cat is a baby.
     */
    private void renderRecursively(GeoBone bone, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha, boolean isBaby) {
        renderRecursively(bone, matrices, vertices, light, overlay, red, green, blue, alpha, isBaby, true, true);
    }

    /**
     * Renders the bones of the model.
     *
     * @param bone                    The parent of all the bones to render.
     * @param matrices                The matrix transformations stack.
     * @param vertices                The vertex consumer.
     * @param light                   The light level.
     * @param overlay                 The overlay.
     * @param red                     The red tint level.
     * @param green                   The green tint level.
     * @param blue                    The blue tint level.
     * @param alpha                   The alpha level.
     * @param isBaby                  Whether the cat is a baby.
     * @param needToTransformBabyBody Whether the body needs to be transformed when the cat is a baby.
     * @param needToTransformBabyHead Whether the head needs to be transformed when the cat is a baby.
     */
    private void renderRecursively(GeoBone bone, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha, boolean isBaby, boolean needToTransformBabyBody, boolean needToTransformBabyHead) {
        matrices.push();

        if (isBaby) {
            float childHeadYOffset = 10;
            float childHeadZOffset = 4;
            float invertedChildHeadScale = 2;
            float invertedChildBodyScale = 2;
            float childBodyYOffset = 24;

            if (GeckoUtil.isBonePartOf(bone, "head_root") && needToTransformBabyHead) {
                // Undo body transformations
                matrices.translate(0, -childBodyYOffset / 16, 0);
                matrices.scale(invertedChildBodyScale, invertedChildBodyScale, invertedChildBodyScale);

                float headScale = 1.5F / invertedChildHeadScale;
                matrices.scale(headScale, headScale, headScale);
                matrices.translate(0, childHeadYOffset / 16, childHeadZOffset / 16);
                matrices.translate(0, 0.3, -0.1);
                needToTransformBabyHead = false;
            } else if (needToTransformBabyBody) {
                float bodyScale = 1 / invertedChildBodyScale;
                matrices.scale(bodyScale, bodyScale, bodyScale);
                matrices.translate(0, childBodyYOffset / 16, 0);
                needToTransformBabyBody = false;
            }
        }

        RenderUtils.translate(bone, matrices);
        RenderUtils.moveToPivot(bone, matrices);
        RenderUtils.rotate(bone, matrices);
        RenderUtils.scale(bone, matrices);
        RenderUtils.moveBackFromPivot(bone, matrices);

        if (!bone.isHidden()) {
            for (GeoCube cube : bone.childCubes) {
                matrices.push();
                if (!bone.cubesAreHidden()) {
                    renderCube(cube, matrices, vertices, light, overlay, red, green, blue, alpha);
                }
                matrices.pop();
            }
        }
        if (!bone.childBonesAreHiddenToo()) {
            for (GeoBone childBone : bone.childBones) {
                renderRecursively(childBone, matrices, vertices, light, overlay, red, green, blue, alpha, isBaby, needToTransformBabyBody, needToTransformBabyHead);
            }
        }

        matrices.pop();
    }
}
