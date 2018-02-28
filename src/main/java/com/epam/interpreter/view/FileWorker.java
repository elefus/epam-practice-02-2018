package com.epam.interpreter.view;

import com.epam.interpreter.base.BrainfuckInstruction;
import com.epam.interpreter.controller.BrainfuckInterpreterController;
import com.epam.interpreter.model.BFGlobalState;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class FileWorker implements BrainfuckInterpreterView {

    private BufferedReader input;
    private PrintWriter output;
    private Flags flags;

    public FileWorker(Flags flags) throws IOException {
        this.output = flags.getOutput().equals("") ? new PrintWriter(System.out) : new PrintWriter(new PrintStream(flags.getOutput()));
        this.input = new BufferedReader(new InputStreamReader(new FileInputStream(flags.getInput())));
        this.flags = flags;
    }

    @Override
    public void run() {
        try {
            BrainfuckInstruction program = BrainfuckInterpreterController
                    .parse(Files
                            .readAllLines(Paths.get(flags.getInput()))
                            .stream()
                            .collect(Collectors.joining()));
            @NotNull BFGlobalState bfs = new BFGlobalState(flags.getBufferSize(), input, new BufferedWriter(output));
            program.eval(program.addSelf(bfs));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
