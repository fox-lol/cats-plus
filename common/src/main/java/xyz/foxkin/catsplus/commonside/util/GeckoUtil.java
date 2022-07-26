package xyz.foxkin.catsplus.commonside.util;

import net.minecraft.client.util.math.MatrixStack;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.util.RenderUtils;

public class GeckoUtil {

    /**
     * Applies the transformations of the given bone and it's parents.
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
     * Applies the transformations of the given bone.
     *
     * @param bone     The bone to apply the transformations from.
     * @param matrices The matrix stack to apply the transformations to.
     */
    private static void applyBoneTransformation(GeoBone bone, MatrixStack matrices) {
        RenderUtils.translate(bone, matrices);
        RenderUtils.rotate(bone, matrices);
        RenderUtils.scale(bone, matrices);
    }
}
