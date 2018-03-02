package com.epam.interpreter;

public class SimpleModel implements BFModel {
    private byte[] buffer;

    public SimpleModel() {
        this(10);
    }

    public SimpleModel(int size) {
        buffer = new byte[size];
    }

    @Override
    public void setCell(int cell, byte val) {
        buffer[cell] = val;
    }

    @Override
    public byte getCell(int cell) {
        return buffer[cell];
    }

    @Override
    public void addCell(int cell, byte val) {
        buffer[cell] += val;
    }

    @Override
    public void increaseBuffer(int size) {
        byte[] newBuf = new byte[buffer.length + size];
        System.arraycopy(buffer, 0, newBuf, 0, buffer.length);
        buffer = newBuf;
    }

    @Override
    public byte[] getAllCells() {
        return buffer;
    }

    @Override
    public int getBufSize() {
        return buffer.length;
    }
}
