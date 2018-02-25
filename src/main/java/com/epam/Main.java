package com.epam;

import com.epam.interpreter.ControllerImpl;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        ControllerImpl controller;
        try {
            controller = new ControllerImpl(args);
            controller.interpret();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
