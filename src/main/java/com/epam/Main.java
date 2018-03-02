package com.epam;

import com.epam.interpreter.InterpreterInitializer;
import com.epam.interpreter.SimpleCommandLineParser;
import org.apache.commons.cli.CommandLine;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException, IllegalAccessException, InstantiationException {

//        String[] strs = {"-optimization"};
//
//        ByteArrayInputStream in = new ByteArrayInputStream("+++++>++++++>[-]>[-]<<<[>>>+<<<-]>>>[<<[<+>>+<-]>[<+>-]>-]<<<._".getBytes());
//        System.setIn(in);
//
//        CommandLine cmd = SimpleCommandLineParser.parse(strs);
//        if (cmd == null) {
//            System.out.println("Wrong cmd arguments");
//            return;
//        }
//        new InterpreterInitializer(cmd);



        byte[] bytes = Files.readAllBytes(Paths.get("./out/BFClass.class"));
        Class<?> myClass = new ClassLoader() {
            Class<?> load() {
                return defineClass(null, bytes, 0, bytes.length);
            }
        }.load();

        myClass.newInstance();
    }


}
