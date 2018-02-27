package com.epam.optimizedInterpreter;

import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;

public class InputOptimizer implements Runnable {
    private BlockingQueue<AbstractCommand> commandsQueue;
    private BlockingQueue<AbstractCommand> optimizedCommandsQueue;
    private LinkedList<AbstractCommand> commandsStack = new LinkedList<>();
    private AbstractCommand cmd;
    private AbstractCommand stackTop;
    private static final int STACK_SIZE = 10;


    public InputOptimizer(BlockingQueue<AbstractCommand> commands, BlockingQueue<AbstractCommand> optimizedCommandsQueue) {
        this.commandsQueue = commands;
        this.optimizedCommandsQueue = optimizedCommandsQueue;
    }

    private boolean tryToCombineCommands() {
        stackTop = commandsStack.peek();
        if (cmd.getClass() == stackTop.getClass()) {
            int newValue;
            commandsStack.pop();
            newValue = cmd.getValue() + stackTop.getValue();
            if (newValue != 0) {
                cmd.setValue(newValue);
                commandsStack.push(cmd);
            }
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        try {
            while (true) {
                cmd = commandsQueue.take();
//                System.out.println(cmd);

                if (cmd instanceof End) {
                    AbstractCommand command;
                    while ((command = commandsStack.pollLast()) != null) {
                        optimizedCommandsQueue.put(command);
                    }
                    for (AbstractCommand com : optimizedCommandsQueue) {
                        System.out.println(com);
                    }
                    return;
                } else {
                    if (commandsStack.size() >= STACK_SIZE) {
                        optimizedCommandsQueue.put(commandsStack.pollLast());
                    }
                }

                if (!commandsStack.isEmpty()) {
                    if(tryToCombineCommands()){
                        continue;
                    }
                } else {
                    commandsStack.push(cmd);
                    continue;
                }



                commandsStack.push(cmd);

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
