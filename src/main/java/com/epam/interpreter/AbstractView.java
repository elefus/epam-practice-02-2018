package com.epam.interpreter;

import java.io.*;

public abstract class AbstractView implements BFView, Closeable{

    BufferedReader reader;
    BufferedWriter writer;
    FileReader fr;
    FileWriter fw;
    boolean systemInput;

    @Override
    public char readInput() throws IOException {
        char symbol;
        if (!systemInput) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
                while ((symbol = (char) br.read()) == '\n') ;
            }
        } else {
            while ((symbol = (char) reader.read()) == '\n') ;
        }
        return symbol;
    }

    @Override
    public char readSymbol() throws IOException {
        char symbol;
        while ((symbol = (char) reader.read()) == '\n') ;
        return symbol;
    }

    @Override
    public void printSymbol(char symbol) throws IOException {
        writer.write(symbol);
        writer.flush();
    }

    @Override
    public void printTrace(byte[] buf, int bufptr) throws IOException {
        int i = 0;
        for (Byte val : buf) {
            writer.write(val.toString() + (i == bufptr ? "*|" : '|'));
            i++;
        }
        writer.write('\n');
        writer.flush();
    }

    @Override
    public void close() throws IOException {
        if (reader != null) {
            reader.close();
        }
        if (writer != null) {
            writer.close();
        }
        if (fr != null) {
            fr.close();
        }
        if (fw != null) {
            fw.close();
        }
    }

}
