package com.epam.interpreter;

public interface BFModel {
    void setCell(int cell, byte val);

    byte getCell(int cell);

    void incrementCell(int cell);

    void decrementCell(int cell);
}
