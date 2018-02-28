package com.epam.interpreter.view;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.jetbrains.annotations.NotNull;

public class CommandLineParse {
    public static CommandLine parse(String[] args) throws ParseException {
        @NotNull Options options = new Options();
        options.addOption("s", "source", true, "Where will be input file. Empty means console and REPL mode");
        options.addOption("b", "buffer", true, "Memory size for execution. Default 30000");
        options.addOption("o", "out", true, "Where will be output file. Empty means console");
        options.addOption("t", "trace", false, "Get state output after execution");
        return new PosixParser().parse(options, args);
    }
}
