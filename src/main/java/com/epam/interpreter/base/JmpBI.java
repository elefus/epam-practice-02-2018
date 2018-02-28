package com.epam.interpreter.base;

import com.epam.interpreter.model.BFGlobalState;
import org.jetbrains.annotations.NotNull;

public class JmpBI extends AbstractBI {
    public JmpBI(int value, BrainfuckInstruction nextInstruction) {
        super(value, nextInstruction);
    }

    @Override
    public BFGlobalState eval(@NotNull BFGlobalState initState) {
        return initState.jmp(value).eval(initState);
    }

    @NotNull
    @Override
    public String toString() {
        return "JMP " + value + '\n' + nextInstruction.toString();
    }
}
