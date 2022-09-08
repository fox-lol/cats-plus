package xyz.foxkin.catsplus.client.matrixscript.exception;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class InvalidInstructionException extends InvalidScriptLineException {

    public InvalidInstructionException(String message, int line) {
        super(message, line);
    }
}
