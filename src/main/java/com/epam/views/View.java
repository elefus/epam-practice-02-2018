package com.epam.views;

import java.io.*;

public class View {
    public BufferedReader reader;
    private BufferedWriter writer;
    private static InputStreamReader inputReader = new InputStreamReader(System.in);

    public View (BufferedReader reader, BufferedWriter writer){
        this.reader = reader; this.writer = writer;
    }


    public char readInput() throws IOException{
        System.out.println("waiting for input...");
        return (char) inputReader.read();
    }

    public void write(byte c) throws IOException{
        Integer tor = new Byte(c).intValue() + 128;
        writer.write(tor.toString() + "\n");
    }

    public int read() throws IOException {
        return reader.read();
    }

    public void reset() throws IOException{
        reader.reset();
    }
    public void mark(int limit) throws IOException{
        reader.mark(limit);
    }

}
