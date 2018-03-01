package com.epam.interpreter;

public interface BFModel {

    void setCell(int cell, byte val);

    byte getCell(int cell);

    void addCell(int cell, byte val);

    void increaseBuffer(int size);

    byte[] getAllCells();

    int getBufSize();
}
