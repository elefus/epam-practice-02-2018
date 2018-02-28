package com.epam.interpreter.model;

public class BrainfuckIntepreterModel {
    private final BFGlobalState state;

    public BrainfuckIntepreterModel(BFGlobalState state) {

        this.state = state;
    }

    public BFGlobalState getState() {
        return state;
    }
}
