package com.epam.optimization.commands;

public abstract class Command {
    int val;

    protected Command(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + getVal() + ")";
    }
}
