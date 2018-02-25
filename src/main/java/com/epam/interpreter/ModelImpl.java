package com.epam.interpreter;

import java.util.ArrayList;
import java.util.Collections;

public class ModelImpl implements BfModel {

    private ArrayList<Byte> memory;
    private static final int DEFAULT_BUFFER_SIZE = 10;

    public ModelImpl() {
        this(DEFAULT_BUFFER_SIZE);
    }

    public ModelImpl(int bufSize) {
        memory = new ArrayList<>(Collections.nCopies(bufSize, (byte) 0));
        Collections.fill(memory, (byte) 0);
    }

    @Override
    public void setValue(byte symbol, int index) {
        memory.set(index, symbol);
    }

    @Override
    public byte getValue(int index) {
        return memory.get(index);
    }

    @Override
    public void increment(int index) {
        memory.set(index, (byte) (memory.get(index) + 1));
    }

    @Override
    public void decrement(int index) {
        memory.set(index, (byte) (memory.get(index) - 1));
    }

    public void changeBufferSize(int cellsToAdd) {
        ArrayList<Byte> newPart = new ArrayList<>(Collections.nCopies(cellsToAdd, (byte) 0));
        memory.addAll(newPart);
    }

    public ArrayList<Byte> getAllBuffer(){
        return memory;
    }

}
