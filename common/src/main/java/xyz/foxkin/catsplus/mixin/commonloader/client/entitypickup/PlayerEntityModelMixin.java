package xyz.foxkin.catsplus.mixin.commonloader.client.entitypickup;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib3.core.processor.AnimationProcessor;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import xyz.foxkin.catsplus.client.access.render.AnimatableContainer;
import xyz.foxkin.catsplus.client.animatable.entity.player.ThirdPersonPlayerArms;
import xyz.foxkin.catsplus.client.init.ModGeoRenderers;
import xyz.foxkin.catsplus.client.model.CatsPlusModel;
import xyz.foxkin.catsplus.client.util.GeckoUtil;
import xyz.foxkin.catsplus.commonside.access.entitypickup.PlayerEntityAccess;

import java.util.function.Consumer;

@Mixin(PlayerEntityModel.class)
abstract class PlayerEntityModelMixin<T extends LivingEntity> extends BipedEntityModel<T> {

    @SuppressWarnings("unused")
    protected PlayerEntityModelMixin(ModelPart root) {
        super(root);
    }

    /**
     * Translates third person {@link GeoBone} transformations to {@link ModelPart} transformations.
     */
    @SuppressWarnings("unchecked")
    @Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", shift = At.Shift.AFTER))
    private void catsPlus$translateGeoModelToMinecraftModel(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        if (livingEntity instanceof PlayerEntityAccess playerAccess && playerAccess.catsPlus$isHoldingEntity()) {
            AnimatableContainer<ThirdPersonPlayerArms> container = (AnimatableContainer<ThirdPersonPlayerArms>) livingEntity;
            ThirdPersonPlayerArms arms = container.catsPlus$getAnimatable();
            CatsPlusModel<ThirdPersonPlayerArms> model = ModGeoRenderers.getModel(ThirdPersonPlayerArms.class).orElseThrow();
            model.setUp(arms);
            AnimationProcessor<?> processor = model.getAnimationProcessor();
            catsPlus$applyBoneTransformations(processor, "right_arm", rightArm, this::catsPlus$sneakTransformation);
            catsPlus$applyBoneTransformations(processor, "left_arm", leftArm, this::catsPlus$sneakTransformation);
        }
    }

    /**
     * Applies addition transformations when the player is sneaking.
     *
     * @param matrix The matrix to apply the transformations to.
     */
    @Unique
    private void catsPlus$sneakTransformation(Matrix4f matrix) {
        if (sneaking) {
            matrix.multiply(Vec3f.POSITIVE_X.getRadialQuaternion(0.4F));
        }
    }

    /**
     * Translates a {@link GeoBone} transformation to a {@code ModelPart} transformation if the {@code GeoBone} exists.
     *
     * @param processor           The {@code AnimationProcessor} to get the {@link GeoBone} from.
     * @param boneName            The name of the {@code GeoBone}.
     * @param modelPart           The {@code ModelPart} to apply the transformation to.
     * @param earlyTransformation An additional transformation to apply before the {@code GeoBone} transformation.
     */
    @Unique
    private static void catsPlus$applyBoneTransformations(AnimationProcessor<?> processor, String boneName, ModelPart modelPart, @Nullable Consumer<Matrix4f> earlyTransformation) {
        if (processor.getBone(boneName) instanceof GeoBone armBone) {
            GeckoUtil.applyBoneTransformations(armBone, modelPart, earlyTransformation);
        }
    }
}
