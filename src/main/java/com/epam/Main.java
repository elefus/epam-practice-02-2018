package com.epam;

import org.apache.commons.cli.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {

        Options options = new Options();
        options.addOption("s","source",true,"set the source file");
        options.addOption("b","buffer",true,"set buffer size");
        options.addOption("o","out",true,"set the output file");
        options.addOption("t","trace",false,"enable trace mode");

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp( "interpreter", options );

        CommandLine cmd=null;
        CommandLineParser cmdParser=new DefaultParser();
        try {
            cmd=cmdParser.parse(options,args);
        } catch (ParseException e) {
            System.out.println( "Parsing failed.  Reason: " + e.getMessage() );
        }

        if(cmd.hasOption("source")){
            String sourceFile = cmd.getOptionValue("source");
        }
        else{
            //source = System.in
        }

        if(cmd.hasOption("buffer")){
            String bufSize = cmd.getOptionValue("buffer");
        }
        else{
            //bufSize = inf
        }



        /*int M=2;
        if(args.length>0)
            M = Integer.parseInt(args[0]);

        HashMap<String,ArrayList<Integer>> hashMap=GenomeSearcher.findSubseq(M);
        GenomeSearcher.printResult(hashMap);*/
    }
}
