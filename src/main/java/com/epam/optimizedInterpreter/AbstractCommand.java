package com.epam.optimizedInterpreter;

public class AbstractCommand {
    protected int value;

    public AbstractCommand(int value) {
        this.value = value;
    }

    public String toString() {
        return getClass().getSimpleName() + "(" + getValue() + ")";
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
