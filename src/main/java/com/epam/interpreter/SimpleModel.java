package com.epam.interpreter;

import java.util.ArrayList;
import java.util.Collections;

public class SimpleModel implements BFModel {
    private ArrayList<Byte> buffer;

    public SimpleModel() {
        this(10);
    }

    public SimpleModel(int size) {
        buffer = new ArrayList<>(Collections.nCopies(size, (byte) 0));
    }

    @Override
    public void setCell(int cell, byte val) {
        buffer.set(cell, val);
    }

    @Override
    public byte getCell(int cell) {
        return buffer.get(cell);
    }

    @Override
    public void incrementCell(int cell) {
        buffer.set(cell, (byte) (buffer.get(cell) + 1));
    }

    @Override
    public void decrementCell(int cell) {
        buffer.set(cell, (byte) (buffer.get(cell) - 1));
    }

    public void increaseBuffer(int size) {
        ArrayList<Byte> appendPart = new ArrayList<>(Collections.nCopies(size, (byte) 0));
        buffer.addAll(appendPart);
    }

    public ArrayList<Byte> getAllCells() {
        return buffer;
    }

    public int getBufSize() {
        return buffer.size();
    }
}
