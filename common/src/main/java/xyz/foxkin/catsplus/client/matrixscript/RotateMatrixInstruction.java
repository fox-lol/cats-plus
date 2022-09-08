package xyz.foxkin.catsplus.client.matrixscript;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class RotateMatrixInstruction implements MatrixInstruction {

    private final Vec3f axisVector;
    private final float angle;

    public RotateMatrixInstruction(Axis axis, float angle) {
        axisVector = axis.axisVector;
        this.angle = angle;
    }

    @Override
    public void execute(MatrixStack matrices) {
        matrices.multiply(axisVector.getDegreesQuaternion(angle));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                "{" +
                "axisVector=" + axisVector +
                ", angle=" + angle +
                '}';
    }

    public enum Axis {

        X(Vec3f.POSITIVE_X),
        Y(Vec3f.POSITIVE_Y),
        Z(Vec3f.POSITIVE_Z);

        private final Vec3f axisVector;

        Axis(Vec3f axisVector) {
            this.axisVector = axisVector;
        }
    }
}
