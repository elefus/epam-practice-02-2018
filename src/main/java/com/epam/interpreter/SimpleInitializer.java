package com.epam.interpreter;

import org.apache.commons.cli.CommandLine;

import java.io.IOException;

public class SimpleInitializer {

    private BFModel model;
    private BFView view;
    private BFController controller;
    private CommandLine cmd;
    private boolean limited;

    public SimpleInitializer(CommandLine cmd) {
        try {
            this.cmd = cmd;
            initView();
            initModel();
            initController();
            controller.interpret();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void initController() throws IOException {
        boolean trace = false;
        if (cmd.hasOption("trace")) {
            trace = true;
        }
        controller = new SimpleController(model, view, limited, trace);
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
