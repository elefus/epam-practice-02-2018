package com.epam.interpreter.base;

import com.epam.interpreter.model.BFGlobalState;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractBI implements BrainfuckInstruction {
    protected final int value;
    final BrainfuckInstruction nextInstruction;

    AbstractBI(int value, BrainfuckInstruction nextInstruction) {
        this.value = value;
        this.nextInstruction = nextInstruction;
    }

    @Override
    public BFGlobalState addSelf(@NotNull BFGlobalState state) {
        return nextInstruction.addSelf(state.addInstruction(this));
    }
}
