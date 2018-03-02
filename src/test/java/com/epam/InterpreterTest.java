package com.epam;

import com.epam.interpreter.InterpreterInitializer;
import com.epam.interpreter.SimpleCommandLineParser;
import org.apache.commons.cli.CommandLine;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class InterpreterTest {

    @Test
    void testSummation() throws IOException, InterruptedException {
        // Prepare
        String inputFile = "./src/test/resources/tests/summation.bf";
        String outputFile = "./src/test/resources/actual_results/summation.dat";
        String answerFile = "./src/test/resources/expected_results/summation.dat";
        String[] strs = {"-source", inputFile,
                "-out", outputFile,
                "-optimization"};
        ByteArrayInputStream in = new ByteArrayInputStream("!@".getBytes());
        System.setIn(in);

        byte[] data=prepareAndExecute(strs,outputFile,answerFile);

        // Assertions
        assertEquals(data[0], data[1]);
    }

    @Test
    void testSubtraction() throws IOException, InterruptedException {
        // Prepare
        String inputFile = "./src/test/resources/tests/subtraction.bf";
        String outputFile = "./src/test/resources/actual_results/subtraction.dat";
        String answerFile = "./src/test/resources/expected_results/subtraction.dat";
        String[] strs = {"-source", inputFile,
                "-out", outputFile,
                "-optimization"};
        ByteArrayInputStream in = new ByteArrayInputStream("dC".getBytes());
        System.setIn(in);

        byte[] data=prepareAndExecute(strs,outputFile,answerFile);

        // Assertions
        assertEquals(data[0], data[1]);
    }

    @Test
    void testMultiplication() throws IOException, InterruptedException {
        // Prepare
        String inputFile = "./src/test/resources/tests/multiplication.bf";
        String outputFile = "./src/test/resources/actual_results/multiplication.dat";
        String answerFile = "./src/test/resources/expected_results/multiplication.dat";
        String[] strs = {"-source", inputFile,
                "-out", outputFile,
                "-optimization"};
        ByteArrayInputStream in = new ByteArrayInputStream("!#".getBytes());
        System.setIn(in);

        byte[] data=prepareAndExecute(strs,outputFile,answerFile);

        // Assertions
        assertEquals(data[0], data[1]);
    }

    byte[] prepareAndExecute(String[] strs, String outputFile, String answerFile) throws IOException, InterruptedException {
        byte[] data = new byte[2];
        CommandLine cmd = SimpleCommandLineParser.parse(strs);
        BufferedReader reader;
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(answerFile))));
        data[0] = (byte) reader.read();
        reader.close();

        // Execute

        new InterpreterInitializer(cmd);
        Thread.sleep(100);
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(outputFile))));
        data[1] = (byte) reader.read();
        reader.close();

        return data;
    }
}

