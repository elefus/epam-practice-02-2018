package com.epam;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        int length;
        try{
            length=Integer.valueOf(args[0]);
        }catch (Exception e){return;}

        Map<String,ArrayList<Integer>> hm =dna.search(length);
        try{
            dna.printResult(hm);
        }catch (IOException e){}
    }
}
