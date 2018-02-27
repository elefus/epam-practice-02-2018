package com.epam.optimizedInterpreter;

import java.util.LinkedList;
import java.util.ListIterator;
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
        if (cmd instanceof Read) {
            return false;
        }
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
        if (cmd instanceof Add) {
            if (stackTop instanceof Assign) {
                stackTop.setValue(stackTop.getValue() + cmd.getValue());
                return true;
            }
        }
        return false;
    }

    private void tryToSendLastCommandToExecution() throws InterruptedException {
        if (commandsStack.size() == STACK_SIZE) {
            if (commandsStack.getLast() instanceof Goto &&
                    commandsStack.getLast().getValue() == 1) {
                return;
            } else {
                optimizedCommandsQueue.put(commandsStack.pollLast());
            }
        }
    }

    private void makeLoop() {
        commandsStack.push(cmd);
        int brackets = 0;
        int countCommands = 0;
        for (AbstractCommand command : commandsStack) {
            if (command instanceof Goto) {
                if (command.getValue() < 0) {
                    brackets--;
                } else {
                    brackets++;
                }
            }
            if (brackets == 0) {
                command.setValue(countCommands);
                commandsStack.getFirst().setValue(-countCommands);
                return;
            }
            countCommands++;
        }
    }

    private void flushStackToExecution() throws InterruptedException {
        AbstractCommand command;
        while ((command = commandsStack.pollLast()) != null) {
            optimizedCommandsQueue.put(command);
        }
        for (AbstractCommand com : optimizedCommandsQueue) {
            System.out.println(com);
        }
    }

    private void tryToMakeAssignmentFromLoop() {
        ListIterator<AbstractCommand> iterator = commandsStack.listIterator();
        AbstractCommand command = null;
        AbstractCommand command2 = null;
        if (iterator.hasNext()) {
            command = iterator.next();
        }
        if (command instanceof Goto && command.getValue() == -2) {
            if (iterator.hasNext()) {
                command2 = iterator.next();
            }
            if (command2 instanceof Add) {
                if (command2.getValue() == -1 || command2.getValue() == 1) {
                    for (int i = 0; i < 3; i++) {
                        commandsStack.pop();
                    }
                    iterator = commandsStack.listIterator();
                    if (iterator.hasNext()) {
                        command = iterator.next();
                    }
                    if (command instanceof Add) {
                        commandsStack.pop();
                    }
                    commandsStack.push(new Assign(0));
                }
            }
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                cmd = commandsQueue.take();

                if (cmd instanceof End) {
                    flushStackToExecution();
                    return;
                } else {
                    tryToSendLastCommandToExecution();
                }

                if (commandsStack.isEmpty()) {
                    commandsStack.push(cmd);
                    continue;
                }

                if (tryToCombineCommands()) {
                    continue;
                }
                if (cmd instanceof Goto && cmd.getValue() == -1) {
                    makeLoop();
                    tryToMakeAssignmentFromLoop();
                    continue;
                }

                commandsStack.push(cmd);

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
