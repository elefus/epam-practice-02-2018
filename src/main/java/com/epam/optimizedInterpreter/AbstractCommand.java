package com.epam.optimizedInterpreter;

public class AbstractCommand {
    private int value;

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
