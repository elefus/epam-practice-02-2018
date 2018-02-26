package com.epam.interpreter;

public class ModelImpl implements BfModel {

    private byte[] memory;
    private static final int DEFAULT_BUFFER_SIZE = 10;

    public ModelImpl() {
        this(DEFAULT_BUFFER_SIZE);
    }

    public ModelImpl(int bufSize) {
        memory = new byte[bufSize];
    }

    @Override
    public void setValue(byte symbol, int index) {
        memory[index]=symbol;
    }

    @Override
    public byte getValue(int index) {
        return memory[index];
    }

    @Override
    public void increment(int index) {
        memory[index]++;
    }

    @Override
    public void decrement(int index) {
        memory[index]--;
    }

    @Override
    public void changeBufferSize(int cellsToAdd) {
        byte[] newPart=new byte[memory.length+cellsToAdd];
        System.arraycopy(memory,0,newPart,0,memory.length);
        memory=newPart;
    }

    @Override
    public byte[] getAllBuffer(){
        return memory;
    }

}
