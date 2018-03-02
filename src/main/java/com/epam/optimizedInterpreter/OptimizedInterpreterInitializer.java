package com.epam.optimizedInterpreter;

import com.epam.Compiler.Compiler;
import com.epam.interpreter.*;
import org.apache.commons.cli.CommandLine;

import java.io.IOException;
import java.util.concurrent.*;

public class OptimizedInterpreterInitializer {
    private CommandLine cmd;
    private BfView view = null;
    private BfModel model;
    private String sourceFile;
    private String outputFile;
    private int bufferSize;
    private boolean bufferIsInfinite;
    private boolean enableTraceMode;
    private boolean enableOptimization;
    private BlockingQueue<AbstractCommand> commandsQueue = new LinkedBlockingQueue<>();
    private BlockingQueue<AbstractCommand> optimizedCommandsQueue = new LinkedBlockingDeque<>();
    private static final int DEFAULT_BUFFER_SIZE = 10;

    public OptimizedInterpreterInitializer(CommandLine cmd) throws IOException {
        this.cmd = cmd;
        initialize();
    }

    private void initialize() throws IOException {
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
        if (cmd.hasOption("trace")) {
            enableTraceMode = true;
        } else {
            enableTraceMode = false;
        }
        if (cmd.hasOption("optimize")) {
            enableOptimization = true;
        } else {
            enableOptimization = false;
        }


        if (enableOptimization) {
            ExecutorService pool = Executors.newFixedThreadPool(3);
            pool.execute(new InputReader(commandsQueue, view));
            pool.execute(new InputOptimizer(commandsQueue, optimizedCommandsQueue));
            pool.execute(new Compiler(optimizedCommandsQueue));
            pool.shutdown();
        } else {
            ControllerImpl controller;
            controller = new ControllerImpl(view, model, bufferIsInfinite, bufferSize, enableTraceMode);
            controller.interpret();
        }
    }
}
