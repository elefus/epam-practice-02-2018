package com.epam;

import com.epam.interpreter.ArgsParser;
import com.epam.optimizedInterpreter.OptimizedInterpreterInitializer;
import org.apache.commons.cli.CommandLine;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OptimizedInterpreterTests {

    String inputFile;
    String outputFile;
    String answerFile;
    BufferedReader reader;
    ByteArrayInputStream in;
    CommandLine cmd;
    byte expected, actual;

    private void prepare() throws IOException {
        System.setIn(in);
        String[] strs = {"-source", inputFile,
                "-out", outputFile,
                "-optimize"};
        cmd = ArgsParser.parseArgs(strs);
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(answerFile))));
        expected =(byte) reader.read();
        reader.close();
    }

    private void execute() throws IOException, InterruptedException {
        new OptimizedInterpreterInitializer(cmd);
        Thread.sleep(100);
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(outputFile))));
        actual = (byte) reader.read();
        reader.close();
    }

    @Test
    @DisplayName("Summation")
    void testMethod1() throws IOException, InterruptedException {
        // Prepare
        inputFile = "./src/test/resources/tests/summation.bf";
        outputFile = "./src/test/resources/tests/computedResults/summation_result.bf";
        answerFile = "./src/test/resources/tests/answers/summation_answer.bf";
        in = new ByteArrayInputStream("! ".getBytes());
        prepare();

        // Execute
        execute();

        // Assertions
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Subtraction")
    void testMethod2() throws IOException, InterruptedException {
        // Prepare
        inputFile = "./src/test/resources/tests/subtraction.bf";
        outputFile = "./src/test/resources/tests/computedResults/subtraction_result.bf";
        answerFile = "./src/test/resources/tests/answers/subtraction_answer.bf";
        in = new ByteArrayInputStream("dC".getBytes());
        prepare();

        // Execute
        execute();

        // Assertions
        assertEquals(expected, actual);
    }
}
