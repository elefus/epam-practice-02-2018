package com.epam.interpreter.view;

import com.epam.interpreter.base.BrainfuckInstruction;
import com.epam.interpreter.controller.BrainfuckInterpreterController;
import com.epam.interpreter.model.BFGlobalState;
import org.jetbrains.annotations.NotNull;

import java.io.*;

public class REPL implements BrainfuckInterpreterView {

    private final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    @NotNull
    private final PrintWriter output;
    @NotNull
    private final Flags flags;

    public REPL(Flags flags) throws IOException {
        this.output = flags.getOutput().equals("") ? new PrintWriter(System.out) : new PrintWriter(new PrintStream(flags.getOutput()));
        this.flags = flags;
    }

    private void runCommand(String command) {
    }

    @Override
    public void run() {
        String command = "";
        output.println("My greetings in the best Brainfuck REPL!");
        output.println("Write your code in one line");
        output.println("Or call command");
        output.println("Full list can be discovered with command :help");
        while (!command.equals(":exit")) {
            try {
                command = input.readLine();
                if (command.charAt(0) == ':') {
                    runCommand(command);
                } else {
                    BrainfuckInstruction program = BrainfuckInterpreterController.parse(command);
                    @NotNull BFGlobalState bfs = new BFGlobalState(flags.getBufferSize(), input, new BufferedWriter(output));
                    program.eval(program.addSelf(bfs));
                    output.println();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
