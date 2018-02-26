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
            controller = new SimpleController(model, view, limited);
            controller.interpret();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void initView() throws IOException {
        String sourceFile;
        String destFile;

        if (cmd.hasOption("source")) {
            sourceFile = cmd.getOptionValue("source");
        } else {
            sourceFile = null;
        }

        if (cmd.hasOption("out")) {
            destFile = cmd.getOptionValue("out");
        } else {
            destFile = null;
        }

        view = new SimpleView(sourceFile, destFile);
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
