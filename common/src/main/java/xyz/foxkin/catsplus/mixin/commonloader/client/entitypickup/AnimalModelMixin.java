package xyz.foxkin.catsplus.mixin.commonloader.client.entitypickup;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AnimalModel.class)
abstract class AnimalModelMixin {

    /**
     * See {@link PlayerEntityModelMixin#catsPlus$removeVanillaArms(Iterable, MatrixStack, VertexConsumer, int)}.
     */
    @ModifyExpressionValue(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/AnimalModel;getBodyParts()Ljava/lang/Iterable;"))
    protected Iterable<ModelPart> catsPlus$removeVanillaArms(Iterable<ModelPart> bodyParts, MatrixStack matrices, VertexConsumer vertices, int light) {
        return bodyParts;
    }
}
