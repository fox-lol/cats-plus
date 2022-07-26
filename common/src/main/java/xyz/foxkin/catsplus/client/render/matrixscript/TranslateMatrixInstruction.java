package xyz.foxkin.catsplus.client.render.matrixscript;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class TranslateMatrixInstruction implements MatrixInstruction {

    private final double xTranslation;
    private final double yTranslation;
    private final double zTranslation;

    public TranslateMatrixInstruction(double xTranslation, double yTranslation, double zTranslation) {
        this.xTranslation = xTranslation;
        this.yTranslation = yTranslation;
        this.zTranslation = zTranslation;
    }

    @Override
    public void execute(MatrixStack matrices) {
        matrices.translate(xTranslation, yTranslation, zTranslation);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                "{" +
                "xTranslation=" + xTranslation +
                ", yTranslation=" + yTranslation +
                ", zTranslation=" + zTranslation +
                '}';
    }
}
