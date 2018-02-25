package com.epam.interpreter;

import java.io.*;
import java.util.ArrayList;

public class ViewImpl implements BfView {

    private BufferedReader reader;
    private BufferedWriter writer;
    private FileReader fileReader;
    private FileWriter fileWriter;
    private boolean fromConsole;

    public ViewImpl(InputStream inputStream, OutputStream outputStream) {
        reader = new BufferedReader(new InputStreamReader(inputStream));
        writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        fromConsole = true;
    }

    public ViewImpl(String inputFilePath, OutputStream outputStream) throws FileNotFoundException {
        fileReader = new FileReader(inputFilePath);
        reader = new BufferedReader(fileReader);
        writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        fromConsole = false;
    }

    public ViewImpl(InputStream inputStream, String outputFilePath) throws IOException {
        fileWriter = new FileWriter(outputFilePath);
        reader = new BufferedReader(new InputStreamReader(inputStream));
        writer = new BufferedWriter(fileWriter);
        fromConsole = true;
    }

    public ViewImpl(String inputFilePath, String outputFilePath) throws IOException {
        fileReader = new FileReader(inputFilePath);
        fileWriter = new FileWriter(outputFilePath);
        reader = new BufferedReader(fileReader);
        writer = new BufferedWriter(fileWriter);
        fromConsole = false;
    }

    @Override
    public char readSymbol() throws IOException {
        char command;
        while ((command = (char) reader.read()) == '\n') ;
        return command;
    }

    @Override
    public void print(char output) throws IOException {
        writer.write(output);
        writer.flush();
    }

    public void trace(ArrayList<Byte> buffer, int index) throws IOException {
        int number=0;
        for(Byte cell : buffer){
            writer.write("["+cell.toString()+(index==number?"^":"")+"]");
            writer.flush();
            number++;
        }
        writer.write('\n');
        writer.flush();
    }

    public char readInput() throws IOException {
        if (!fromConsole) {
            reader = new BufferedReader(new InputStreamReader(System.in));
        }
        char input;
        while ((input = (char) reader.read()) == '\n');

        if (reader != null && !fromConsole) {
            reader.close();
        }
        return input;
    }

    public void closeFiles() throws IOException {
        if (reader != null) {
            reader.close();
        }
        if (writer != null) {
            writer.close();
        }
        if (fileReader != null) {
            fileReader.close();
        }
        if (fileWriter != null) {
            fileWriter.close();
        }
    }
}
