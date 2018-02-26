package com.epam;

import com.epam.interpreter.*;
import org.apache.commons.cli.CommandLine;

public class Main {


    public static void main(String[] args) {

        CommandLine cmd= SimpleCommandLineParser.parse(args);
        if(cmd==null){
            System.out.println("Wrong cmd arguments");
            return;
        }
        new SimpleInitializer(cmd);
    }


}
