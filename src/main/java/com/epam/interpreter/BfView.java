package com.epam.interpreter;

import java.io.IOException;

public interface BfView {
    char readSymbol() throws IOException;

    void print(char output) throws IOException;
}
