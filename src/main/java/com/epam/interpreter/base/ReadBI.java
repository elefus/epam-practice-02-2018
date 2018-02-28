package com.epam.interpreter.base;

import com.epam.interpreter.model.BFGlobalState;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadBI extends AbstractBI {
    public ReadBI(int value, BrainfuckInstruction nextInstruction) {
        super(value, nextInstruction);
    }

    @Override
    public BFGlobalState eval(@NotNull BFGlobalState initState) {
        BufferedReader input = initState.getInput();
        try (input) {
            if (input.skip(value) != value) {
                System.err.println("Read less symbols than must be read");
            }
            return nextInstruction.eval(initState.assign(input.read()).execute());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nextInstruction.eval(initState.execute());
    }

    @NotNull
    @Override
    public String toString() {
        return "READ " + value + '\n' + nextInstruction.toString();
    }
}
