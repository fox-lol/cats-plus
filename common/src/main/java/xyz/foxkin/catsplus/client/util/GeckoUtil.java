package xyz.foxkin.catsplus.client.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.util.RenderUtils;
import xyz.foxkin.catsplus.commonside.access.entitypickup.Matrix4fAccess;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class GeckoUtil {

    /**
     * Applies the transformations of a {@code GeoBone} and it's parents to a {@code MatrixStack}.
     *
     * @param bone     The bone to apply the transformations from.
     * @param matrices The matrix stack to apply the transformations to.
     */
    public static void applyBoneTransformations(GeoBone bone, MatrixStack matrices) {
        GeoBone parent = bone.getParent();
        if (parent != null) {
            applyBoneTransformations(parent, matrices);
        }
        applyBoneTransformation(bone, matrices);
    }

    /**
     * Applies the transformations of a {@code GeoBone} to a {@code MatrixStack}.
     *
     * @param bone     The bone to apply the transformations from.
     * @param matrices The matrix stack to apply the transformations to.
     */
    private static void applyBoneTransformation(GeoBone bone, MatrixStack matrices) {
        RenderUtils.translate(bone, matrices);
        RenderUtils.moveToPivot(bone, matrices);
        RenderUtils.rotate(bone, matrices);
        RenderUtils.scale(bone, matrices);
        RenderUtils.moveBackFromPivot(bone, matrices);
    }

    /**
     * Determines whether a bone is or is a child of the bone with the given name.
     *
     * @param child      The bone to check.
     * @param parentName The name of the parent bone.
     * @return {@code true} if the bone is or is a child of the bone with the given name, {@code false} otherwise.
     */
    public static boolean isBonePartOf(GeoBone child, String parentName) {
        if (child.getName().equals(parentName)) {
            return true;
        } else {
            GeoBone parent = child.getParent();
            while (parent != null) {
                if (parent.getName().equals(parentName)) {
                    return true;
                } else {
                    parent = parent.getParent();
                }
            }
            return false;
        }
    }

    /**
     * Translates {@code GeoBone} transformations to {@code ModelPart} transformations.
     *
     * @param bone                The bone to translate the transformations from.
     * @param modelPart           The model part to translate the transformations to.
     * @param earlyTransformation An optional transformation to apply before the bone transformations.
     */
    public static void applyBoneTransformations(GeoBone bone, ModelPart modelPart, @Nullable Consumer<Matrix4f> earlyTransformation) {
        Matrix4f matrix = new Matrix4f();
        matrix.loadIdentity();
        if (earlyTransformation != null) {
            earlyTransformation.accept(matrix);
        }
        applyBoneTransformationsForModelPart(bone, matrix);

        Matrix4fAccess access = (Matrix4fAccess) (Object) matrix;
        Vec3f translation = access.catsPlus$getTranslation();
        Vec3f rotation = access.catsPlus$getEulerAngles();
        // Vec3f scale = access.catsPlus$getScale();

        modelPart.pivotX += translation.getX();
        modelPart.pivotY += translation.getY();
        modelPart.pivotZ += translation.getZ();

        modelPart.pitch = rotation.getX();
        modelPart.yaw = rotation.getY();
        modelPart.roll = rotation.getZ();

        /*
        Scale is unsupported in 1.18
        modelPart.xScale = scale.getX();
        modelPart.yScale = scale.getY();
        modelPart.zScale = scale.getZ();
         */
    }

    /**
     * Applies the transformations of a {@code GeoBone} and it's parents to a {@code Matrix4f}.
     * Used for translating {@code GeoBone} transformations to {@code ModelPart} transformations.
     *
     * @param bone   The bone to translate the transformations from.
     * @param matrix The matrix to translate the transformations to.
     */
    private static void applyBoneTransformationsForModelPart(GeoBone bone, Matrix4f matrix) {
        GeoBone parent = bone.getParent();
        if (parent != null) {
            applyBoneTransformationsForModelPart(parent, matrix);
        }
        applyBoneTransformationForModelPart(bone, matrix);
    }

    /**
     * Applies the transformations of a {@code GeoBone} to a {@code Matrix4f}.
     * Used for translating {@code GeoBone} transformations to {@code ModelPart} transformations.
     *
     * @param bone   The bone to translate the transformations from.
     * @param matrix The matrix to translate the transformations to.
     */
    private static void applyBoneTransformationForModelPart(GeoBone bone, Matrix4f matrix) {
        // Translate
        matrix.multiplyByTranslation(bone.getPositionX(), -bone.getPositionY(), bone.getPositionZ());

        // Moving to bone pivot breaks things, so we don't do that

        // Rotate
        if (bone.getRotationZ() != 0) {
            matrix.multiply(Vec3f.POSITIVE_Z.getRadialQuaternion(bone.getRotationZ()));
        }
        if (bone.getRotationY() != 0) {
            matrix.multiply(Vec3f.NEGATIVE_Y.getRadialQuaternion(bone.getRotationY()));
        }
        if (bone.getRotationX() != 0) {
            matrix.multiply(Vec3f.NEGATIVE_X.getRadialQuaternion(bone.getRotationX()));
        }

        // Scale
        matrix.multiply(Matrix4f.scale(bone.getScaleX(), bone.getScaleY(), bone.getScaleZ()));
    }
}
