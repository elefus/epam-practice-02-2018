package com.epam.interpreter.base;

import com.epam.interpreter.model.BFGlobalState;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class EndBI implements BrainfuckInstruction {
    @NotNull
    @Override
    public BFGlobalState eval(@NotNull BFGlobalState initState) {
        try {
            initState.getInput().close();
            initState.getOutput().close();
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

    @Override
    public BFGlobalState addSelf(BFGlobalState state) {
        return state.addInstruction(this);
    }
}
