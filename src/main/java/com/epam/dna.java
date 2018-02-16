package com.epam;

import java.io.*;
import java.util.*;

/**
 * Created by fomin on 16.02.18.
 */
public class dna {
    public static Map<String,ArrayList<Integer>> search(int M){
        Map<String,ArrayList<Integer>> hm =new HashMap<>();
        ClassLoader cl=ClassLoader.getSystemClassLoader();

        try(BufferedReader reader=new BufferedReader(new InputStreamReader(cl.getResourceAsStream("bioinformatic/source.dat")))){
            int c;
            char[] buf=new char[M];
            int pos=0;

            reader.mark(M);
            while ((c=reader.read(buf,0,M))==M){
                hm.putIfAbsent(String.valueOf(buf),new ArrayList<Integer>());
                hm.get(String.valueOf(buf)).add(pos);
                reader.reset();
                reader.skip(1);
                reader.mark(M);
                pos++;
            }
        }
        catch (IOException ex){
            System.out.println(ex.getMessage());
        }
        return hm;
    }

    public static void printResult(Map<String,ArrayList<Integer>> hm) throws IOException{
        Set<Map.Entry<String,ArrayList<Integer>>> hashSet=hm.entrySet();
        for(Map.Entry entry:hashSet){
            System.out.print(entry.getKey()+"=[");
            ArrayList<Integer> values=(ArrayList<Integer>)entry.getValue();
            Iterator it=values.iterator();
            while(it.hasNext()){
                System.out.print(it.next());
                if(it.hasNext()) System.out.print(", ");
            }
            System.out.print("]\n");
        }
    }
}
