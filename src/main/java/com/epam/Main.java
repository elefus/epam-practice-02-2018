package com.epam;

import com.epam.interpreter.view.Flags;
import com.epam.interpreter.view.REPL;

import java.io.BufferedWriter;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        new REPL(new Flags("", "", 25, false)).run();
    }
}
