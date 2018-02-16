package com.epam;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FastGenomeSearcherTest {

    FastGenomeSearcher.Window testWindow;
    @BeforeEach
    void setUp() {
        long[] data = new long[1];
        data[0] = 0x0;
        int size = 2;
        testWindow = new FastGenomeSearcher().new Window(data, size);
    }

    @Test
    void shiftTest() {
        assertEquals(" ", " ");
        System.out.println(Utils.printBinLongArray(testWindow.shiftAndAdd(0b01).getCleanData()));
        System.out.println(Utils.printBinLongArray(testWindow.shiftAndAdd(0b10).getCleanData()));
        System.out.println(Utils.printBinLongArray(testWindow.shiftAndAdd(0b11).getCleanData()));
    }
}