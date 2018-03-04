package com.epam;

import com.epam.interpreter.controller.Controller;
import com.epam.interpreter.controller.Optimizer;
import com.epam.interpreter.model.Cells;
import com.epam.interpreter.view.Viewer;
import org.apache.commons.cli.*;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Interpreter {
    private static final Set<String> brfSymbols = new HashSet<>(Arrays.asList(
            new String[] {">","<",".",",","+","-","[","]"}
    ));
    String brfCode;






    public Interpreter(String[] args){

        BufferedWriter bufferedWriter = null;

        BufferedReader bufferedReader = null;


        Options options = new Options();
        options.addOption("s", "source", true, "set the source file");
        options.addOption("b", "buffer", true, "set buffer size");
        options.addOption("o", "out", true, "set the output file");
        options.addOption("t", "trace", false, "enable trace mode");


        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("interpreter", options);

        CommandLine cmd = null;
        CommandLineParser cmdParser = new DefaultParser();
        try {
            cmd = cmdParser.parse(options, args);
        } catch (ParseException e) {
            System.out.println("Parsing failed.  Reason: " + e.getMessage());
        }

        if(cmd.hasOption("source")){
            try {
                 brfCode = Files.lines(Paths.get("source"), StandardCharsets.UTF_8).collect(Collectors.joining());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("Any non-brainfuck symbol will finish input");
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                String symbol;
                while(brfSymbols.contains(symbol=bufferedReader.readLine())){

                   brfCode += symbol;

            }}catch (Exception e){
                    e.printStackTrace();
                }
        }

        if(cmd.hasOption("out")){
            try {
                bufferedWriter = new BufferedWriter(new FileWriter(cmd.getOptionValue("out")));
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        else {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));
        }



        Controller controller = new Controller(new Viewer(bufferedReader,bufferedWriter),new Optimizer(brfCode),new Cells(100));
        controller.Interpret();
        }
    }
