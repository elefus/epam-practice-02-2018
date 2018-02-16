package com.epam;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        int M=2;
        if(args.length>0)
            M = Integer.parseInt(args[0]);

        HashMap<String,ArrayList<Integer>> hashMap=GenomeSearcher.findSubseq(M);
        GenomeSearcher.printResult(hashMap);
    }
}
