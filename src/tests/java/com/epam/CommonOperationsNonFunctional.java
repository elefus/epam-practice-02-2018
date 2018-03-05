package com.epam;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CommonOperationsNonFunctional {

    @Test
    @DisplayName("Summation")
    void sum() throws Exception {
        byte[] input = {4, 5};

        Integer result = test("summation.bf", input);

        assertEquals(9, result.intValue());
    }

    @Test
    @DisplayName("Subtraction")
    void sub() throws Exception {
        byte[] input = {7, 4};

        Integer result = test("subtraction.bf", input);

        assertEquals(3, result.intValue());
    }

    @Test
    @DisplayName("Multiplication")
    void mult() throws Exception {
        byte[] input = {3, 2};

        Integer result = test("multiplication.bf", input);

        assertEquals(6, result.intValue());
    }

    @Test
    @DisplayName("Division")
    void div() throws Exception {
        byte[] input = {8, 2};

        Integer result = test("division.bf", input);

        assertEquals(4, result.intValue());
    }

    @Test
    @DisplayName("Assigning")
    void assigni() throws Exception {
        byte[] input = {7, 4};

        Integer result = test("assigning.bf", input);
        assertEquals(4, result.intValue());
    }

    @Test
    @DisplayName("Zeroing")
    void zer() throws Exception {
        byte[] input = {10};

        Integer result = test("zeroing.bf", input);

        assertEquals(0, result.intValue());
    }

    @Test
    @DisplayName("Negation")
    void neg() throws Exception {
        byte[] input = {5};

        Integer result = test("negation.bf", input);

        assertEquals(256-5, result.intValue());
    }

    @Test
    @DisplayName("Swapping")
    void swap() throws Exception {
        byte[] input = {5,10};

        Integer result = test("swaping.bf", input);

        assertEquals(105, result.intValue());
    }

    //@Test
    void testCustomSystemIn() throws Exception {
        withCustomByteSystemIn(() -> {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                assertEquals("Hello", reader.readLine());
                assertEquals("World!", reader.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }, new ByteArrayInputStream("Hello\nWorld!\n".getBytes()));
    }

    private static Integer test(String source, byte...input) throws Exception {
        return withCustomByteSystemIn(() -> {
            Path out = Files.createTempFile("interpreterTestResult", ".tmp");
            out.toFile().deleteOnExit();

            String[] args = { "-s" + Paths.get(ClassLoader.getSystemResource(source).toURI()), "-o" + out};

            new Interpreter(args).run();

            return Files.lines(out)
                    .collect(collectingAndThen(joining(), Integer::valueOf));
        }, new ByteArrayInputStream(input));
    }

    private static <T> T withCustomByteSystemIn(Callable<T> task, InputStream input) throws Exception {
        InputStream original = System.in;
        try {
            System.setIn(input);
            return task.call();
        } finally {
            System.setIn(original);
        }
    }
}