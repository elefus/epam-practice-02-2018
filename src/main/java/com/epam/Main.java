package com.epam;

import com.epam.interpreter.InterpreterInitializer;
import com.epam.interpreter.SimpleCommandLineParser;
import com.epam.optimization.*;
import com.epam.optimization.commands.Command;
import org.apache.commons.cli.CommandLine;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {


    public static void main(String[] args) {

        CommandLine cmd = SimpleCommandLineParser.parse(args);
        if (cmd == null) {
            System.out.println("Wrong cmd arguments");
            return;
        }
        boolean optimization = true;
        new InterpreterInitializer(cmd, optimization);


//        ExecutorService es = Executors.newFixedThreadPool(3);
//        BlockingQueue<Command> queue=new LinkedBlockingQueue<>(10);
//        es.execute(new ControllerReader(queue));
//        es.execute(new ControllerOptimizer(queue));
//        es.shutdown();
    }


}
