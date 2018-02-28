package com.epam.interpreter.base;

import com.epam.interpreter.model.BFGlobalState;
import org.jetbrains.annotations.NotNull;

public class AddBI extends AbstractBI {
    public AddBI(int value, BrainfuckInstruction nextInstruction) {
        super(value, nextInstruction);
    }

    @Override
    public BFGlobalState eval(@NotNull BFGlobalState initState) {
        return nextInstruction.eval(initState.add(value).execute());
    }

    @NotNull
    @Override
    public String toString() {
        return "ADD " + value + '\n' + nextInstruction.toString();
    }
}
