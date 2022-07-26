package xyz.foxkin.catsplus.client.render.matrixscript.exception;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.io.IOException;

@Environment(EnvType.CLIENT)
public class InvalidScriptLineException extends IOException {

    private final int line;

    public InvalidScriptLineException(String message, int line) {
        super(message);
        this.line = line;
    }

    public int getLine() {
        return line;
    }
}
