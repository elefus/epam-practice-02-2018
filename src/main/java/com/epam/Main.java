package com.epam;

import com.epam.interpreter.*;
import org.apache.commons.cli.CommandLine;

import java.io.IOException;

public class Main {

    private static final int DEFAULT_BUFFER_SIZE = 10;

    public static void main(String[] args) throws IOException {

        BfView view = null;
        BfModel model;
        String sourceFile;
        String outputFile;
        int bufferSize;
        boolean bufferIsInfinite;
        boolean enableTraceMode;

        CommandLine cmd;
        cmd = ArgsParser.parseArgs(args);

        if (cmd.hasOption("source")) {
            sourceFile = cmd.getOptionValue("source");
        } else {
            sourceFile = null;
        }

        if (cmd.hasOption("out")) {
            outputFile = cmd.getOptionValue("out");
        } else {
            outputFile = null;
        }

        if (sourceFile == null && outputFile == null) {
            view = new ViewFromConToCon(System.in, System.out);         //Console=>Console
        } else if (sourceFile != null && outputFile != null) {
            view = new ViewFromFileToFile(sourceFile, outputFile);      //File=>File
        } else if (sourceFile == null && outputFile != null) {
            view = new ViewFromConToFile(System.in, outputFile);        //Console=>File
        } else if (sourceFile != null && outputFile == null) {
            view = new ViewFromFileToCon(sourceFile, System.out);       //File=>Console
        }

        if (cmd.hasOption("buffer")) {
            bufferSize = Integer.parseInt(cmd.getOptionValue("buffer"));
            model = new ModelImpl(bufferSize);
            bufferIsInfinite = false;
        } else {
            bufferSize = DEFAULT_BUFFER_SIZE;
            model = new ModelImpl();
            bufferIsInfinite = true;
        }
        if(cmd.hasOption("trace")){
            enableTraceMode=true;
        }else {
            enableTraceMode=false;
        }

        ControllerImpl controller;

        controller = new ControllerImpl(view, model, bufferIsInfinite, bufferSize, enableTraceMode);
        controller.interpret();

    }
}
