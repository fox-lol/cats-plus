package xyz.foxkin.catsplus.client.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.util.RenderUtils;

@Environment(EnvType.CLIENT)
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
}
