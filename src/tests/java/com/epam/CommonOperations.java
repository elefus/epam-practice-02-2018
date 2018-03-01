package com.epam;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommonOperations {



    @Test
    @DisplayName("Summation")
    void sum () throws IOException, InterruptedException{
        Integer x = 5,y = 4;


        String[] args = {"-s", "src/tests/resources/summation.bf", "-o", "src/tests/resources/summationResult.txt"};
        byte[] bytes = {x.byteValue(), y.byteValue()};
        System.setIn(new ByteArrayInputStream(bytes));

        new Interpreter(args);
        Operation<Integer,Integer> sum = (a, b) -> a + b;

        Integer supposed = sum.apply(x,y);
        Integer result = Integer.parseInt(Files.readAllLines(Paths.get("src/tests/resources/summationResult.txt")).get(0));


        assertEquals(supposed, result);
    }

    @Test
    @DisplayName("Subtraction")
    void sub () throws IOException, InterruptedException {
        Integer x = 7, y = 4;


        String[] args = {"-s", "src/tests/resources/subtraction.bf", "-o", "src/tests/resources/subtractionResult.txt"};
        byte[] bytes = {x.byteValue(), y.byteValue()};
        System.setIn(new ByteArrayInputStream(bytes));

        new Interpreter(args);

        Operation<Integer, Integer> sub = (a, b) -> a - b;

        Integer supposed = sub.apply(x, y);
        Integer result = Integer.parseInt(Files.readAllLines(Paths.get("src/tests/resources/subtractionResult.txt")).get(0));


        assertEquals(supposed, result);
    }
    @Test
    void testCustomSystemIn() throws IOException {
        InputStream customConsoleStream = new ByteArrayInputStream("Hello\nWorld!\n".getBytes());
        System.setIn(customConsoleStream);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        assertEquals("Hello", reader.readLine());
        assertEquals("World!", reader.readLine());
    }

    @Test
    @DisplayName("Multiplication")
    void mult () throws IOException, InterruptedException {
        Integer x = 3, y = 2;

        String[] args = {"-s", "src/tests/resources/multiplication.bf", "-o", "src/tests/resources/multiplicationResult.txt"};
        byte[] bytes = {x.byteValue(), y.byteValue()};
        System.setIn(new ByteArrayInputStream(bytes));

        new Interpreter(args);

        Operation<Integer, Integer> sub = (a, b) -> a * b;

        Integer supposed = sub.apply(x, y);

        Integer result = Integer.parseInt(Files.readAllLines(Paths.get("src/tests/resources/multiplicationResult.txt")).get(0));


        assertEquals(supposed, result);
    }

    @FunctionalInterface
    public interface Operation<A,B> {
        Integer apply(A a,B b);
    }
}
