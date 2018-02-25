package com.epam;

import com.epam.interpreter.SimpleCommandLineParser;
import com.epam.interpreter.SimpleController;
import org.apache.commons.cli.CommandLine;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        CommandLine cmd= SimpleCommandLineParser.parse(args);
        if(cmd==null){
            System.out.println("Wrong cmd arguments");
            return;
        }

        try {
            SimpleController controller=new SimpleController(cmd);
            controller.interpret();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
