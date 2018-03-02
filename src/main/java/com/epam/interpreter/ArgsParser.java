package com.epam.interpreter;

import org.apache.commons.cli.*;

public class ArgsParser {

    public static CommandLine parseArgs(String[] args) {
        Options options = new Options();
        options.addOption("s", "source", true, "set the source file");
        options.addOption("b", "buffer", true, "set buffer size");
        options.addOption("o", "out", true, "set the output file");
        options.addOption("t", "trace", false, "enable trace mode");
        options.addOption("opt", "optimize", false, "enable optimisation");

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("interpreter", options);

        CommandLine cmd = null;
        CommandLineParser cmdParser = new DefaultParser();
        try {
            cmd = cmdParser.parse(options, args);
        } catch (ParseException e) {
            System.out.println("Parsing failed.  Reason: " + e.getMessage());
        }
        return cmd;
    }
}
