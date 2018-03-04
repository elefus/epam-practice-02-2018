package com.epam.interpreter.model;

import java.io.IOException;


public class Cells {
    private  byte[] buffer;

    public Cells (int size) {
        buffer = new byte[size];
    }
    public Cells () {
        buffer = new byte [30000];
    }

    public void setCell (int cell, byte value) {
        buffer[cell] = value;
    }

    public byte getCell (int cell) {
        return buffer[cell];
    }

    public void addCell (int cell, byte value) {
        buffer[cell] += value;
    }

    public void increaseBuffer (int size) {
        byte[] newBuffer = new byte[buffer.length + size];
        System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
        buffer = newBuffer;
    }

    public byte[] getAllCells() {
        return buffer;
    }

    public int getBufSize() {
        return buffer.length;
    }

}
