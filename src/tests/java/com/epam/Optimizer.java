package com.epam;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Optimizer {


    @Test
    @DisplayName("Add Move")
    void t () {
        Interpreter i;
        try {
            String[] args = { "-s" + Paths.get(ClassLoader.getSystemResource("addmove.bf").toURI())};
            i = new Interpreter(args);

            String exp = "Move(1) Add(1) ";
            assertEquals(exp, i.example.toString());

        } catch (Exception e) { e.printStackTrace();assertTrue(false);}
    }

    @Test
    @DisplayName("Loops")
    void loop () {
        Interpreter i;
        try {
            String[] args = { "-s" + Paths.get(ClassLoader.getSystemResource("loop.bf").toURI())};
            i = new Interpreter(args);


            String exp = "Move(1) Add(1) LOOP:Move(2)Add(2)Move(-2)Add(-1) ";
            assertEquals(exp, i.example.toString());

        } catch (Exception e) { e.printStackTrace();assertTrue(false); }
    }
    @Test
    @DisplayName("ReadPrint")
    void rp () {
        Interpreter i;
        try {
            String[] args = {"-s" + Paths.get(ClassLoader.getSystemResource("readprint.bf").toURI())};
            i = new Interpreter(args);


            String exp = "Read(0) Print(0) ";
            assertEquals(exp, i.example.toString());

        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    @DisplayName("Assign")
    void assign () {
        Interpreter i;
        try {
            String[] args = {"-s" + Paths.get(ClassLoader.getSystemResource("assign.bf").toURI())};
            i = new Interpreter(args);


            String exp = "Add(6) Assign(2) ";
            assertEquals(exp, i.example.toString());

        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }
}
