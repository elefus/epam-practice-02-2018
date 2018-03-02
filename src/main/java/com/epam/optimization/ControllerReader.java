package com.epam.optimization;

import com.epam.interpreter.BFView;
import com.epam.optimization.commands.*;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class ControllerReader implements Runnable {
    private BlockingQueue<Command> cmdQueue;
    private BFView view;
    private char symbol;
    private Command cmd;

    public ControllerReader(BlockingQueue<Command> cmdQueue, BFView view) {
        this.cmdQueue = cmdQueue;
        this.view = view;
    }

    @Override
    public void run() {
        try {
            while (true) {
                symbol = view.readSymbol();
                switch (symbol) {
                    case Character.MAX_VALUE:
                        cmd = new End(1);
                        cmdQueue.put(cmd);
                        return;
                    case '+':
                        cmd = new Add(1);
                        break;
                    case '-':
                        cmd = new Add(-1);
                        break;
                    case '>':
                        cmd = new Move(1);
                        break;
                    case '<':
                        cmd = new Move(-1);
                        break;
                    case ',':
                        char input = view.readInput();
                        cmd = new Read(input);
                        break;
                    case '.':
                        cmd = new Print(1);
                        break;
                    case '[':
                        cmd = new Goto(1);
                        break;
                    case ']':
                        cmd = new Goto(-1);
                        break;

                    case '_':
                        cmd = new End(1);
                        cmdQueue.put(cmd);
                        return;


                    default:
//                        cmd = new End(1);
//                        cmdQueue.put(cmd);
//                        return;
                        continue;
                }

                cmdQueue.put(cmd);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
