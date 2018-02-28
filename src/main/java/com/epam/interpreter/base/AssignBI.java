package com.epam.interpreter.base;

import com.epam.interpreter.model.BFGlobalState;
import org.jetbrains.annotations.NotNull;

public class AssignBI extends AbstractBI {
    public AssignBI(int value, BrainfuckInstruction nextInstruction) {
        super(value, nextInstruction);
    }

    @Override
    public BFGlobalState eval(@NotNull BFGlobalState initState) {
        return nextInstruction.eval(initState.assign(value).execute());
    }

    @NotNull
    @Override
    public String toString() {
        return "ASSIGN " + value + '\n' + nextInstruction.toString();
    }
}
