package com.epam.interpreter.base;

import com.epam.interpreter.model.BFGlobalState;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.stream.IntStream;

public class PrintBI extends AbstractBI {
    public PrintBI(int value, BrainfuckInstruction nextInstruction) {
        super(value, nextInstruction);
    }

    @Override
    public BFGlobalState eval(@NotNull BFGlobalState initState) {
        try {
            initState.getOutput().write(IntStream
                    .generate(() -> initState.getCells().get(initState.getFocus().getValue()))
                    .limit(value)
                    .boxed()
                    .map(e -> (char) e.byteValue())
                    .reduce(new StringBuilder(""), StringBuilder::append, StringBuilder::append)
                    .toString(), 0, value);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nextInstruction.eval(initState.execute());
    }

    @NotNull
    @Override
    public String toString() {
        return "PRINT " + value + '\n' + nextInstruction.toString();
    }
}
