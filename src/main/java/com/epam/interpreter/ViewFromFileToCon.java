package com.epam.interpreter;

import java.io.*;

public class ViewFromFileToCon extends ViewAbstract {
    public ViewFromFileToCon(String inputFilePath, OutputStream outputStream) throws FileNotFoundException {
        fileReader = new FileReader(inputFilePath);
        reader = new BufferedReader(fileReader);
        writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        fromConsole = false;
    }
}
