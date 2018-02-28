package com.epam.interpreter.base;

import com.epam.interpreter.model.BFGlobalState;
import org.jetbrains.annotations.NotNull;

public class MoveBI extends AbstractBI {
    public MoveBI(int value, BrainfuckInstruction nextInstruction) {
        super(value, nextInstruction);
    }

    @Override
    public BFGlobalState eval(@NotNull BFGlobalState initState) {
        return nextInstruction.eval(initState.move(value).execute());
    }

    @NotNull
    @Override
    public String toString() {
        return "MOVE " + value + '\n' + nextInstruction.toString();
    }
}

