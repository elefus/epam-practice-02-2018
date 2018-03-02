package com.epam.interpreter;

import java.io.*;

public class FileToConView extends AbstractView {
    public FileToConView(String sourceFile) throws IOException {
        fr = new FileReader(sourceFile);
        reader = new BufferedReader(fr);
        writer = new BufferedWriter(new OutputStreamWriter(System.out));
    }
}
