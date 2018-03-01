package com.epam;

import com.epam.controllers.CellController;
import com.epam.controllers.InterpreterController;
import com.epam.models.Cells;
import com.epam.views.View;
import org.apache.commons.cli.*;

import java.io.*;

public class Interpreter {
    public Interpreter(String[] args){
        BufferedWriter w = null;
        BufferedReader r = null;


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
                r = new BufferedReader(new FileReader(cmd.getOptionValue("source")));
            } catch (FileNotFoundException fnf) {}
        }
        else {
            r = new BufferedReader(new InputStreamReader(System.in));
        }

        if(cmd.hasOption("out")){
            try {
                w = new BufferedWriter(new FileWriter(cmd.getOptionValue("out")));
            } catch (IOException ioe) {}
        }
        else {
            w = new BufferedWriter(new OutputStreamWriter(System.out));
        }






        InterpreterController b = new InterpreterController(new View(r,w), new CellController(new Cells()));
        Runnable example = b.generateCommands();
        example.run();

        try { w.close(); r.close();}
        catch (Exception e){}
    }
}
