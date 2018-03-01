package com.epam.optimizedInterpreter;

import com.epam.interpreter.BfView;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class InputReader implements Runnable {

    private BlockingQueue<AbstractCommand> commandsQueue;
    private AbstractCommand cmd;
    private BfView view;
    private char symbol;

    public InputReader(BlockingQueue<AbstractCommand> commands, BfView view) {
        this.commandsQueue = commands;
        this.view = view;
    }

    private AbstractCommand createCommand(char symbol) throws IOException {
        AbstractCommand command;
        switch (symbol) {
            case '+':
                command = new Add(1);
                break;
            case '-':
                command = new Add(-1);
                break;
            case '<':
                command = new Move(-1);
                break;
            case '>':
                command = new Move(1);
                break;
            case '.':
                command = new Print(1);
                break;
            case ',':
                command = new Read(1);
                int value = view.readInput();
                command.setValue(value);
                break;
            case '[':
                command = new Goto(1);
                break;
            case ']':
                command = new Goto(-1);
                break;
            case '_':
                command = new End(1);
                break;
            default:
                command = null;
                break;
        }
        return command;
    }

    @Override
    public void run() {
        try {
            while (true) {
                symbol = view.readSymbol();

                if (symbol == Character.MAX_VALUE) {
                    commandsQueue.put(new End(1));
                    return;
                }

                cmd = createCommand(symbol);
                if(cmd!=null) {
                    commandsQueue.put(cmd);
                    if (cmd instanceof End) {
                        return;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
