package com.epam.interpreter.model;

import java.io.IOException;

//собственно модель брейнфака 30000 байтовых клеток с самыми стандартными методами
public class Cells {
    private  byte[] buffer;

    public Cells (int size) {
        buffer = new byte[size];
    }
    public Cells () {
        buffer = new byte [30000];
    }

    public void setCell (int cell, byte value) {
        buffer[cell] = value;
    }

    public byte getCell (int cell) {
        return buffer[cell];
    }

    public void addCell (int cell, byte value) {
        buffer[cell] += value;
    }

    public int getBufSize() {
        return buffer.length;
    }

}
