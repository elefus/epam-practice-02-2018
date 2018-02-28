package com.epam.interpreter.base;

import com.epam.interpreter.model.BFGlobalState;
import org.jetbrains.annotations.NotNull;

public class NopBI implements BrainfuckInstruction {
    private BrainfuckInstruction nextInstruction;

    public NopBI(BrainfuckInstruction nextInstruction) {
        this.nextInstruction = nextInstruction;
    }

    @Override
    public BFGlobalState eval(@NotNull BFGlobalState initState) {
        return nextInstruction.eval(initState.execute());
    }

    @NotNull
    @Override
    public String toString() {
        return "NOP";
    }

    @Override
    public BFGlobalState addSelf(BFGlobalState state) {
        return nextInstruction.addSelf(state.addInstruction(this));
    }
}
