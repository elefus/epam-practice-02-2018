package com.epam;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * Created by fomin on 16.02.18.
 */
public class DNATest {
    @Test
    void testSearcher() throws IOException{
        // Prepare
        ClassLoader cl=ClassLoader.getSystemClassLoader();
        BufferedReader readerExpected=new BufferedReader(new InputStreamReader(
                cl.getResourceAsStream("bioinformatic/Expected_result_for_the_source_dat.txt")));
        int M=2;
        Map<String,ArrayList<Integer>> hm =null;
        Map<String,ArrayList<Integer>> tm =null;
        Set<Map.Entry<String,ArrayList<Integer>>> hashSet;
        ArrayList<String> resultActual=new ArrayList<>();
        ArrayList<String> resultExpected = new ArrayList<>();
        String line = readerExpected.readLine();
        while (line != null) {
            resultExpected.add(line);
            line = readerExpected.readLine();
        }


        // Execute
        hm = dna.search(M);
        tm=new TreeMap<>(hm);
        hashSet=tm.entrySet();
        for(Map.Entry entry:hashSet){
            StringBuilder str=new StringBuilder();
            str.append(entry.getKey()+"=[");
            ArrayList<Integer> values=(ArrayList<Integer>)entry.getValue();
            Iterator it=values.iterator();
            while(it.hasNext()){
                str.append(it.next());
                if(it.hasNext()) str.append(", ");
            }
            str.append("]");
            resultActual.add(str.toString());
        }

        readerExpected.close();

        // Assertions
        assertArrayEquals(resultExpected.toArray(),resultActual.toArray());
    }
}
