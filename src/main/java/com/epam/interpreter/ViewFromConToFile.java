package com.epam.interpreter;

import java.io.*;

public class ViewFromConToFile extends ViewAbstract {
    public ViewFromConToFile(InputStream inputStream, String outputFilePath) throws IOException {
        fileWriter = new FileWriter(outputFilePath);
        reader = new BufferedReader(new InputStreamReader(inputStream));
        writer = new BufferedWriter(fileWriter);
        fromConsole = true;
    }
}
