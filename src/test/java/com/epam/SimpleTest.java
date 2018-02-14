package com.epam;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SimpleTest {

    @Test
    void testMethod() {
        // Prepare
        int a = 0;
        int b = 1;

        // Execute
        int result = a + b;

        // Assertions
        assertEquals(1, result);
    }
}
