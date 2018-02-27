package com.epam.interpreter;

import com.epam.optimization.ControllerOptimizer;
import com.epam.optimization.ControllerReader;
import com.epam.optimization.commands.Command;
import org.apache.commons.cli.CommandLine;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class InterpreterInitializer {

    private BFModel model;
    private BFView view;
    private BFController controller;
    private CommandLine cmd;
    private boolean limited;
    private boolean optimization;

    public InterpreterInitializer(CommandLine cmd, boolean optimization) {
        try {
            this.cmd = cmd;
            this.optimization = optimization;
            initView();
            initModel();
            initController();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void initController() throws IOException {
        boolean trace = false;
        if (cmd.hasOption("trace")) {
            trace = true;
        }
        if (!optimization) {
            controller = new SimpleController(model, view, limited, trace);
            controller.interpret();
            return;
        }

        ExecutorService es = Executors.newFixedThreadPool(3);
        BlockingQueue<Command> readerQueue = new LinkedBlockingQueue<>(10);
        BlockingQueue<Command> optimizedQueue = new LinkedBlockingQueue<>(10);
        es.execute(new ControllerReader(readerQueue, view));
        es.execute(new ControllerOptimizer(readerQueue, optimizedQueue));
        es.shutdown();
    }

    private void initView() throws IOException {

        if (cmd.hasOption("source") && cmd.hasOption("out")) {
            view = new FileToFileView(cmd.getOptionValue("source"), cmd.getOptionValue("out"));
            return;
        }
        if (cmd.hasOption("source") && !cmd.hasOption("out")) {
            view = new FileToConView(cmd.getOptionValue("source"));
            return;
        }
        if (!cmd.hasOption("source") && cmd.hasOption("out")) {
            view = new ConToFileView(cmd.getOptionValue("out"));
            return;
        }
        if (!cmd.hasOption("source") && !cmd.hasOption("out")) {
            view = new ConToConView();
            return;
        }


    }

    private void initModel() {
        if (cmd.hasOption("buffer")) {
            int size = Integer.parseInt(cmd.getOptionValue("buffer"));
            model = new SimpleModel(size);
            limited = true;
        } else {
            model = new SimpleModel();
            limited = false;
        }
    }
}
