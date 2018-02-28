package com.epam.interpreter.base;

import com.epam.interpreter.model.BFGlobalState;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class EndBI implements BrainfuckInstruction {
    @NotNull
    @Override
    public BFGlobalState eval(@NotNull BFGlobalState initState) {
        try {
            initState.getInput();
            initState.getOutput().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return initState;
    }

    @NotNull
    @Override
    public String toString() {
        return "END OF WORLD";
    }

    @NotNull
    @Override
    public BFGlobalState addSelf(@NotNull BFGlobalState state) {
        return state.addInstruction(this);
    }
}
