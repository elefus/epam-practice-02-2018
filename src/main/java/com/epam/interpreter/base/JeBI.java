package com.epam.interpreter.base;

import com.epam.interpreter.model.BFGlobalState;
import org.jetbrains.annotations.NotNull;

public class JeBI extends AbstractBI {
    public JeBI(int value, BrainfuckInstruction nextInstruction) {
        super(value, nextInstruction);
    }

    @Override
    public BFGlobalState eval(@NotNull BFGlobalState initState) {
        if (initState.getCells().get(initState.getFocus().getValue()) != 0) {
            return nextInstruction.eval(initState.execute());
        }
        return initState.jmp(value).eval(initState);
    }

    @NotNull
    @Override
    public String toString() {
        return "JE " + value + '\n' + nextInstruction.toString();
    }
}
