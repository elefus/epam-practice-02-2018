package com.epam;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aleksandr on 16.02.18.
 */
public class GenomeSearcher {
    public static HashMap<String, ArrayList<Integer>> findSubseq(int M) {
        BufferedReader in;
        int position = 0;
        char[] buf = new char[100];
        String key;
        HashMap<String, ArrayList<Integer>> hashMap = new HashMap<>();
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        String file = "bioinformatic/source.dat";
        try {
            in = new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream(file)));
            in.mark(M);
            while (in.read(buf, 0, M) == M) {
                key = new String(buf);
                hashMap.putIfAbsent(key, new ArrayList<Integer>());
                hashMap.get(key).add(position);

                in.reset();
                in.skip(1);
                in.mark(M);
                position++;
            }
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return hashMap;
    }

    public static void printResult(HashMap<String, ArrayList<Integer>> hashMap) {
        ArrayList<Integer> values;
        String key;
        for (Map.Entry<String, ArrayList<Integer>> ee : hashMap.entrySet()) {
            key = ee.getKey();
            values = ee.getValue();
            System.out.print(key + " - [");
            values.forEach(integer -> System.out.print(integer + ", "));
            System.out.print("\b\b]\n");
        }
    }
}
