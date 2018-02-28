package com.epam;

import com.epam.interpreter.ArgsParser;
import com.epam.optimizedInterpreter.OptimizedInterpreterInitializer;
import org.apache.commons.cli.CommandLine;

import java.io.IOException;

public class Main {



    public static void main(String[] args) {

        CommandLine cmd;
        cmd = ArgsParser.parseArgs(args);
        try {
            new OptimizedInterpreterInitializer(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
