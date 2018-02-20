package com.epam;

import com.epam.interpreter.BfModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by aleksandr on 21.02.18.
 */
public class InterpreterTests {
    @Test
    void mockitoModelTest() {
        // Prepare
        BfModel bfModel=mock(BfModel.class);
        when(bfModel.getValue(anyInt())).thenReturn('s');

        // Execute
        char result=bfModel.getValue(3);

        // Assertions
        assertEquals('s',result);
    }
}
