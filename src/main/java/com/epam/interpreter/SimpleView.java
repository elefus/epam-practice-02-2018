package com.epam.interpreter;

import java.io.*;
import java.util.ArrayList;

public class SimpleView implements BFView {

    private BufferedReader reader;
    private BufferedWriter writer;
    private FileReader fr;
    private FileWriter fw;
    private boolean systemInput;

    public SimpleView(String sourceFile, String destFile) throws IOException {
        if (sourceFile == null) {
            reader = new BufferedReader(new InputStreamReader(System.in));
            systemInput = true;
        } else {
            fr = new FileReader(sourceFile);
            reader = new BufferedReader(fr);
        }
        if (destFile == null) {
            writer = new BufferedWriter(new OutputStreamWriter(System.out));
        } else {
            fw = new FileWriter(destFile);
            writer = new BufferedWriter(fw);
        }
    }

    public char readInput() throws IOException {
        char symbol;
        if (!systemInput) {
            InputStreamReader r = new InputStreamReader((System.in));
            BufferedReader br = new BufferedReader(r);
            while ((symbol = (char) br.read()) == '\n') ;
            br.close();
            r.close();
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

    public void printTrace(ArrayList<Byte> buf, int bufptr) throws IOException {
        int i=0;
        for (Byte val:buf){
            writer.write(val.toString()+(i==bufptr?"*|":'|'));
            i++;
        }
        writer.write('\n');
        writer.flush();
    }

    public void closeFiles() throws IOException {
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
