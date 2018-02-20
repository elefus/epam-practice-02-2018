package com.epam;

import com.epam.interpreter.BFModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by fomin on 21.02.18.
 */
public class MockitoTest {

    @Test
    void TestMethod(){
        BFModel bfModel=mock(BFModel.class);
        when(bfModel.GetCell(anyInt())).thenReturn('a');

        String result=bfModel.GetCell(1)+"b"+bfModel.GetCell(3);

        assertEquals("aba",result);
    }
}
