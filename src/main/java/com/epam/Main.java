package com.epam;

import com.epam.Compiler.Compiler;
import com.epam.interpreter.ArgsParser;
import com.epam.optimizedInterpreter.CommandExecutor;
import com.epam.optimizedInterpreter.InputOptimizer;
import com.epam.optimizedInterpreter.InputReader;
import com.epam.optimizedInterpreter.OptimizedInterpreterInitializer;
import org.apache.commons.cli.CommandLine;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) throws IOException, IllegalAccessException, InstantiationException, InterruptedException {

//        String[] strs = {"-optimize"};
//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
//                "+++++ > ++++++ >[-]>[-]<<<[>>>+ <<<-]>>>[<<[<+ >>+ <-]>[<+ >-]>-]<<< .".getBytes());
//        System.setIn(byteArrayInputStream);
//        CommandLine cmd;
//        cmd = ArgsParser.parseArgs(strs);
//        try {
//            new OptimizedInterpreterInitializer(cmd);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        byte[] bytes = Files.readAllBytes(Paths.get("./out/BFClass.class"));
        Class<?> myClass = new ClassLoader() {
            Class<?> load() {
                return defineClass(null, bytes, 0, bytes.length);
            }
        }.load();

        myClass.newInstance();

    }
}
