package xyz.foxkin.catsplus.commonside.util;

import net.minecraft.client.util.math.MatrixStack;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.util.RenderUtils;

public class GeckoUtil {

    public static void applyBoneTransformations(GeoBone bone, MatrixStack matrices) {
        GeoBone parent = bone.getParent();
        if (parent != null) {
            applyBoneTransformations(parent, matrices);
        }
        applyBoneTransformation(bone, matrices);
    }

    private static void applyBoneTransformation(GeoBone bone, MatrixStack matrices) {
        RenderUtils.translate(bone, matrices);
        RenderUtils.rotate(bone, matrices);
        RenderUtils.scale(bone, matrices);
    }
}
