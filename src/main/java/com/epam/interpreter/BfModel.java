package com.epam.interpreter;

public interface BfModel {
    void setValue(byte symbol, int index);

    byte getValue(int index);

    void addValue(int index,byte value);

    void changeBufferSize(int cellsToAdd);

    byte[] getAllBuffer();
}
