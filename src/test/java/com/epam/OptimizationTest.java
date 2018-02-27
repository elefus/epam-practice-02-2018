package com.epam;

import com.epam.interpreter.InterpreterInitializer;
import com.epam.interpreter.SimpleCommandLineParser;
import org.apache.commons.cli.CommandLine;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OptimizationTest {

    @Test
    void testMethod() throws IOException {
        // Prepare
        String[] strs = {"-source", "./src/test/resources/tests/optimizationTest.bf",
                "-out", "./src/test/resources/tests/optimizationTestResult.dat"};
        CommandLine cmd = SimpleCommandLineParser.parse(strs);
        byte[] expected= Files.readAllBytes(Paths.get("./src/test/resources/tests/optimizationTestExpected.dat"));

        // Execute
        new InterpreterInitializer(cmd, true);
        byte[] actual= Files.readAllBytes(Paths.get("./src/test/resources/tests/optimizationTestResult.dat"));

        // Assertions
        assertArrayEquals(expected,actual);
    }

}
