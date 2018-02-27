package com.epam.interpreter;

import java.io.*;

public class ConToConView extends AbstractView {

    public ConToConView() throws IOException {
        reader = new BufferedReader(new InputStreamReader(System.in));
        systemInput = true;
        writer = new BufferedWriter(new OutputStreamWriter(System.out));
    }
}
