package com.epam.interpreter;

public interface BfModel {
    void setValue(byte symbol, int index);

    byte getValue(int index);

    void increment(int index);

    void decrement(int index);
}
