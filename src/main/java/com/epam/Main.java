package com.epam;

import com.epam.interpreter.ArgsParser;
import com.epam.optimizedInterpreter.OptimizedInterpreterInitializer;
import org.apache.commons.cli.CommandLine;

import java.io.IOException;

public class Main {



    public static void main(String[] args) {

        String inputFile="./src/test/resources/tests/summation.bf";
        String outputFile="./src/test/resources/tests/computedResults/summation_result.bf";
        String answerFile="./src/test/resources/tests/answers/summation_answer.bf";
        String[] strs = {"-source", inputFile,
                "-out", outputFile,
                "-optimize"};
        CommandLine cmd;
        cmd = ArgsParser.parseArgs(strs);
        try {
            new OptimizedInterpreterInitializer(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
