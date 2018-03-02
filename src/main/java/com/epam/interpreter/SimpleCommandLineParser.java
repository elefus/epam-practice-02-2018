package com.epam.interpreter;

import org.apache.commons.cli.*;

public class SimpleCommandLineParser {

    public static CommandLine parse(String[] args) {

        Options options = new Options();

        options.addOption("s", "source", true, "Set the source file");
        options.addOption("b", "buffer", true, "Set the buffer's size");
        options.addOption("o", "out", true, "Set the destination file");
        options.addOption("t", "trace", false, "Enable tracing");
        options.addOption("opt", "optimization", false, "Enable optimization");

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("interpreter", options);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println("Parse failed: " + e.getMessage());
        }
        return cmd;
    }
}
