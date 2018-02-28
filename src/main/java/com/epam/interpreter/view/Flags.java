package com.epam.interpreter.view;

public class Flags {
    private String input;
    private String output;
    private int bufferSize;
    private boolean traceOn;

    public void setInput(String input) {
        this.input = input;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public void setTraceOn(boolean traceOn) {
        this.traceOn = traceOn;
    }

    public String getInput() {
        return input;
    }

    public String getOutput() {
        return output;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public boolean isTraceOn() {
        return traceOn;
    }

    public Flags(String input, String output, int bufferSize, boolean traceOn) {

        this.input = input;
        this.output = output;
        this.bufferSize = bufferSize;
        this.traceOn = traceOn;
    }
}
