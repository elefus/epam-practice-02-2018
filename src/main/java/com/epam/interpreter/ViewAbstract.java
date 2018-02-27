package com.epam.interpreter;

import java.io.*;

public abstract class ViewAbstract implements BfView, Closeable {

    protected BufferedReader reader;
    protected BufferedWriter writer;
    protected FileReader fileReader;
    protected FileWriter fileWriter;
    protected BufferedReader consoleReader;
    protected boolean fromConsole;

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

    @Override
    public void trace(byte[] buffer, int index) throws IOException {
        int number = 0;
        for (Byte cell : buffer) {
            writer.write("[" + cell.toString() + (index == number ? "^" : "") + "]");
            writer.flush();
            number++;
        }
        writer.write('\n');
        writer.flush();
    }

    @Override
    public char readInput() throws IOException {
        char input;
        if (!fromConsole) {
            if(consoleReader==null) {
                consoleReader = new BufferedReader(new InputStreamReader(System.in));
            }
                while ((input = (char) consoleReader.read()) == '\n') ;
        }else{
            while ((input = (char) reader.read()) == '\n') ;
        }

        return input;
    }

    @Override
    public void close() throws IOException {
        if (reader != null) {
            reader.close();
        }
        if (writer != null) {
            writer.close();
        }
    }
}
