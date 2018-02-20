package com.epam;

import org.junit.jupiter.api.Test;

import javax.swing.text.html.HTMLDocument;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by aleksandr on 16.02.18.
 */
public class GenomeSearchTest {

    @Test
    void testMethod() throws IOException {
        // Prepare
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        String file="bioinformatic/answer.txt";
        BufferedReader reader=new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream(file)));
        ArrayList<String> answers = new ArrayList<>();
        String line = reader.readLine();
        while (line != null) {
            answers.add(line);
            line = reader.readLine();
        }


        // Execute
        HashMap<String,ArrayList<Integer>> hashMap=GenomeSearcher.findSubseq(2);

        Map<String, ArrayList<Integer>> map = new TreeMap<String, ArrayList<Integer>>(hashMap);
        ArrayList<String> results=new ArrayList<>();
        Set<Map.Entry<String,ArrayList<Integer>>> hashSet=map.entrySet();
        for(Map.Entry entry:hashSet){
            StringBuilder str=new StringBuilder();
            str.append(entry.getKey().toString().trim()+"=[");
            ArrayList<Integer> values=(ArrayList<Integer>)entry.getValue();
            Iterator it=values.iterator();
            while(it.hasNext()){
                str.append(it.next());
                if(it.hasNext()) str.append(", ");
            }
            str.append("]");
            results.add(str.toString());
        }
        reader.close();
        // Assertions
        assertArrayEquals(answers.toArray(),results.toArray());
    }

}
