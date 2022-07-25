package xyz.foxkin.catsplus.client.render.entity.mob;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import xyz.foxkin.catsplus.client.animatable.mob.ReplacedCatEntity;
import xyz.foxkin.catsplus.client.model.entity.CatsPlusModel;
import xyz.foxkin.catsplus.client.render.CatsPlusGeoRenderer;
import xyz.foxkin.catsplus.mixin.commonloader.client.accessor.CatCollarFeatureRendererAccessor;

@Environment(EnvType.CLIENT)
public class ReplacedCatRenderer extends CatsPlusGeoRenderer<ReplacedCatEntity> {

    public ReplacedCatRenderer(CatsPlusModel<ReplacedCatEntity> modelProvider) {
        super(modelProvider, ReplacedCatEntity.class);
    }

    @Override
    public void render(ReplacedCatEntity animatable, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(animatable, matrices, vertexConsumers, light);
        if (animatable.isTamed()) {
            renderCollar(animatable, matrices, vertexConsumers, light);
        }
    }

    private void renderCollar(ReplacedCatEntity animatable, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        GeoModel model = getGeoModel(animatable);
        RenderLayer collarLayer = RenderLayer.getEntityCutoutNoCull(CatCollarFeatureRendererAccessor.catsPlus$getSkin());
        float[] collarColor = animatable.getCollarColor();
        render(model, animatable, 0, collarLayer, matrices, vertexConsumers, null, light, OverlayTexture.DEFAULT_UV, collarColor[0], collarColor[1], collarColor[2], 1);
    }
}
