package com.epam;

import com.epam.interpreter.InterpreterInitializer;
import com.epam.interpreter.SimpleCommandLineParser;
import com.epam.optimization.*;
import com.epam.optimization.commands.Command;
import org.apache.commons.cli.CommandLine;

import java.io.ByteArrayInputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {


    public static void main(String[] args) {

        String inputFile="./src/test/resources/tests/summation.bf";
        String outputFile="./src/test/resources/actual_results/summation.dat";
        String answerFile="./src/test/resources/expected_results/summation.dat";
        String[] strs = {"-source", inputFile,
//                "-out", outputFile,
                "-optimization"
//                "-trace"
        };

        ByteArrayInputStream in = new ByteArrayInputStream("!@".getBytes());
        System.setIn(in);

        CommandLine cmd = SimpleCommandLineParser.parse(strs);
        if (cmd == null) {
            System.out.println("Wrong cmd arguments");
            return;
        }
        new InterpreterInitializer(cmd);
    }


}
