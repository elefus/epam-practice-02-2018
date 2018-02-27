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

    private AbstractCommand createCommand(char symbol) {
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
            default:
                command = new End(1);
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
                    return;
                }

                cmd = createCommand(symbol);
                commandsQueue.put(cmd);
                if(cmd instanceof End){
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
