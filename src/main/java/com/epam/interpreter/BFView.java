package com.epam.interpreter;

import java.io.IOException;

public interface BFView {
    char readSymbol() throws IOException;

    void printSymbol(char symbol) throws IOException;

    char readInput() throws IOException;

    void printTrace(byte[] buf, int bufptr) throws IOException;
}
