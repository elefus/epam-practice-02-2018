package com.epam;

import com.epam.interpreter.InterpreterInitializer;
import com.epam.interpreter.SimpleCommandLineParser;
import org.apache.commons.cli.CommandLine;

import java.io.ByteArrayInputStream;

public class Main {


    public static void main(String[] args) {

        ByteArrayInputStream in = new ByteArrayInputStream("!@".getBytes());
        System.setIn(in);

        CommandLine cmd = SimpleCommandLineParser.parse(args);
        if (cmd == null) {
            System.out.println("Wrong cmd arguments");
            return;
        }
        new InterpreterInitializer(cmd);
    }


}
