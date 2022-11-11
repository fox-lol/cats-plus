package xyz.foxkin.catsplus.client.matrixscript;

import com.google.common.base.Enums;
import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import xyz.foxkin.catsplus.client.matrixscript.exception.InvalidArgumentException;
import xyz.foxkin.catsplus.client.matrixscript.exception.InvalidInstructionException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

@Environment(EnvType.CLIENT)
public class MatrixScript {

    private final List<MatrixInstruction> instructions;

    private MatrixScript(List<MatrixInstruction> instructions) {
        this.instructions = instructions;
    }

    public void execute(MatrixStack matrices) {
        for (MatrixInstruction instruction : instructions) {
            instruction.execute(matrices);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getClass().getSimpleName())
                .append("{")
                .append("instructions={");
        Iterator<MatrixInstruction> iterator = instructions.iterator();
        while (iterator.hasNext()) {
            builder.append(iterator.next().toString());
            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }
        builder.append("}}");
        return builder.toString();
    }

    @Environment(EnvType.CLIENT)
    public static class Builder {

        private final ImmutableList.Builder<MatrixInstruction> instructions = new ImmutableList.Builder<>();

        @SuppressWarnings("UnusedReturnValue")
        public Builder addInstruction(MatrixInstruction instruction) {
            instructions.add(instruction);
            return this;
        }

        public MatrixScript build() {
            return new MatrixScript(instructions.build());
        }

        public static MatrixScript parseInstructions(InputStream inputStream) throws IOException {
            Builder builder = new Builder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String myLine;
            int lineNumber = 1;
            while ((myLine = reader.readLine()) != null) {
                String[] parts = myLine.split(Pattern.quote(" "));
                int partCount = parts.length;
                if (partCount != 0) {
                    String instruction = parts[0];
                    if (!instruction.startsWith("//")) {
                        switch (instruction) {
                            case "translate" -> {
                                int expectedPartCount = 4;
                                if (partCount == expectedPartCount) {
                                    List<Double> arguments = new ArrayList<>(3);
                                    for (int i = 1; i < 4; i++) {
                                        String argument = parts[i];
                                        try {
                                            arguments.add(Double.parseDouble(argument));
                                        } catch (NumberFormatException e) {
                                            throw new InvalidArgumentException("Argument \"" + argument + "\" is not a number", lineNumber);
                                        }
                                    }
                                    builder.addInstruction(new TranslateMatrixInstruction(arguments.get(0), arguments.get(1), arguments.get(2)));
                                } else if (partCount < expectedPartCount) {
                                    throw new InvalidArgumentException("Not enough arguments, expected " + (expectedPartCount - 1), lineNumber);
                                } else {
                                    throw new InvalidArgumentException("Too many arguments, expected " + (expectedPartCount - 1), lineNumber);
                                }
                            }
                            case "rotate" -> {
                                int expectedPartCount = 3;
                                if (partCount == expectedPartCount) {
                                    String stringAxis = parts[1];
                                    final int finalLineNumber = lineNumber;
                                    RotateMatrixInstruction.Axis axis = Enums.getIfPresent(RotateMatrixInstruction.Axis.class, stringAxis.toUpperCase()).toJavaUtil().orElseThrow(
                                            () -> new InvalidArgumentException("Axis argument \"" + stringAxis + "\" is invalid, should be either x, y or z", finalLineNumber)
                                    );
                                    String stringDegrees = parts[2];
                                    try {
                                        float degrees = Float.parseFloat(stringDegrees);
                                        builder.addInstruction(new RotateMatrixInstruction(axis, degrees));
                                    } catch (NumberFormatException e) {
                                        throw new InvalidArgumentException("Argument \"" + stringDegrees + "\" is not a number", finalLineNumber);
                                    }
                                } else if (partCount < expectedPartCount) {
                                    throw new InvalidArgumentException("Not enough arguments, expected " + (expectedPartCount - 1), lineNumber);
                                } else {
                                    throw new InvalidArgumentException("Too many arguments, expected " + (expectedPartCount - 1), lineNumber);
                                }
                            }
                            default -> throw new InvalidInstructionException("Instruction \"" + instruction + "\" is invalid", lineNumber);
                        }
                    }
                }
                lineNumber++;
            }
            return builder.build();
        }
    }
}
