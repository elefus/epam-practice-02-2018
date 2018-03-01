package com.epam.interpreter;

import java.io.IOException;

public interface BfView {
    char readSymbol() throws IOException;

    void print(char output) throws IOException;

    void trace(byte[] buffer, int index) throws IOException;

    char readInput() throws IOException;
}
