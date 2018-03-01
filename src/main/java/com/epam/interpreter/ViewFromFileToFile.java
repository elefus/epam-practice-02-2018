package com.epam.interpreter;

import java.io.*;

public class ViewFromFileToFile extends ViewAbstract {
    public ViewFromFileToFile(String inputFilePath, String outputFilePath) throws IOException {
        fileReader = new FileReader(inputFilePath);
        fileWriter = new FileWriter(outputFilePath);
        reader = new BufferedReader(fileReader);
        writer = new BufferedWriter(fileWriter);
        fromConsole = false;
    }
}
