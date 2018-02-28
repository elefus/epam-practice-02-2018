package com.epam.interpreter.base;

import com.epam.interpreter.model.BFGlobalState;

public interface BrainfuckInstruction {
    BFGlobalState eval(BFGlobalState initState);

    BFGlobalState addSelf(BFGlobalState state);
}
