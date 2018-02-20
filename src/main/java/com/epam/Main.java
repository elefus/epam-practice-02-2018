package com.epam;

import org.apache.commons.cli.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
//        int length;
//        try{
//            length=Integer.valueOf(args[0]);
//        }catch (Exception e){return;}
//
//        Map<String,ArrayList<Integer>> hm =dna.search(length);
//        try{
//            dna.printResult(hm);
//        }catch (IOException e){}


        Options options=new Options();

        options.addOption("s","source",true,"Set the source file");
        options.addOption("b","buffer",true,"Set the buffer's size");
        options.addOption("o","out",true,"Set the destination file");
        options.addOption("t","trace",false,"Enable tracing");

        HelpFormatter formatter=new HelpFormatter();
        formatter.printHelp( "help", options );

        CommandLineParser parser=new DefaultParser();
        CommandLine cmd=null;
        try {
            cmd = parser.parse(options, args);
        }catch (ParseException e){
            System.out.println("Parse failed: "+e.getMessage());
        }

        //examples
        if(cmd.hasOption("source"))
            System.out.println("Source file defined: "+cmd.getOptionValue("source"));
    }
}
