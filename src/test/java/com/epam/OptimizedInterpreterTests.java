package com.epam;

import com.epam.interpreter.ArgsParser;
import com.epam.interpreter.BfView;
import com.epam.interpreter.ViewFromFileToFile;
import com.epam.optimizedInterpreter.OptimizedInterpreterInitializer;
import org.apache.commons.cli.CommandLine;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;
import org.junit.jupiter.api.Test;

import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class OptimizedInterpreterTests {

    @Rule
    public final TextFromStandardInputStream systemInMock
            = emptyStandardInputStream();
    @Test
    void testMethod() throws IOException {
        // Prepare
        OptimizedInterpreterInitializer opt = mock(OptimizedInterpreterInitializer.class);
        String inputFile="./src/test/resources/tests/summation.bf";
        String outputFile="./src/test/resources/tests/computedResults/summation_result.bf";
        String answerFile="./src/test/resources/tests/answers/summation_answer.bf";
        String[] strs = {"-source", inputFile,
                "-out", outputFile};

        CommandLine cmd = ArgsParser.parseArgs(strs);
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        BufferedReader reader;
        reader=new BufferedReader(new InputStreamReader(new FileInputStream(new File(answerFile))));
        byte expected = (byte) reader.read();
        reader.close();


        // Execute

        new OptimizedInterpreterInitializer(cmd);
        reader=new BufferedReader(new InputStreamReader(new FileInputStream(new File(outputFile))));
        byte actual = (byte) reader.read();
        reader.close();

        // Assertions
        assertEquals(expected,actual);
    }

}
