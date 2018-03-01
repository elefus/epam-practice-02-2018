package com.epam.models;

public class Cells {
    byte[] a  = new byte[30000];;
    public Cells() {
        for(int i=0;i<a.length;i++)
            a[i]=Byte.MIN_VALUE;
    }

    public byte[] getAll() {
        return a;
    }


}



