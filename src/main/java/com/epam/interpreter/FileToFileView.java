package com.epam.interpreter;

import java.io.*;

public class FileToFileView extends AbstractView {
    public FileToFileView(String sourceFile, String destFile) throws IOException {
        fr = new FileReader(sourceFile);
        reader = new BufferedReader(fr);
        fw = new FileWriter(destFile);
        writer = new BufferedWriter(fw);
    }
}
