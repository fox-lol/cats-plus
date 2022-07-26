package xyz.foxkin.catsplus.client.render.matrixscript;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public interface MatrixInstruction {

    void execute(MatrixStack matrices);
}
