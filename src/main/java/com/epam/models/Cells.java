package com.epam.models;


import java.util.Arrays;

public class Cells {
    final int explore = 10;
    byte[] a  = new byte[0];
    public Cells() {
        Arrays.fill(a, Byte.MIN_VALUE);
    }

    public byte get(int index) {
        while(index >= a.length) {
            exploreBuffer();
        }
        return a[index];
    }

    public void set(int index, byte val) {
        while(index >= a.length) {
            exploreBuffer();
        }
        a[index] = val;
    }

    private void exploreBuffer() {
        byte[] ar = new byte[a.length + explore];
        Arrays.fill(ar, Byte.MIN_VALUE);
        System.arraycopy(a, 0, ar, 0, a.length);
        a = ar;
    }


}



