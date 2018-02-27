package com.epam.interpreter;

import java.io.*;

public class ConToFileView extends AbstractView {
    public ConToFileView(String destFile) throws IOException {
        reader = new BufferedReader(new InputStreamReader(System.in));
        systemInput = true;
        fw = new FileWriter(destFile);
        writer = new BufferedWriter(fw);
    }
}
