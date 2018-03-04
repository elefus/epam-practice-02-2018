package com.epam.interpreter.view;

import java.io.*;


public class Viewer {
    public BufferedReader reader;
    private BufferedWriter writer;


    public Viewer (BufferedReader reader, BufferedWriter writer){
        this.reader = reader;
        this.writer = writer;
    }


    public String readInput() throws IOException{
        System.out.println("Write smth");
        return reader.readLine();
    }

    public void write(byte c) throws IOException{
        Integer tor = new Byte(c).intValue() ;
        writer.write(tor.toString() + "\n");
    }

    public int read() throws IOException {
        return reader.read();
    }




}