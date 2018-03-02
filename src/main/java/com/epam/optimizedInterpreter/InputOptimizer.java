package com.epam.optimizedInterpreter;

import java.io.IOException;
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
    private int brackets = 0;


    public InputOptimizer(BlockingQueue<AbstractCommand> commands, BlockingQueue<AbstractCommand> optimizedCommandsQueue) {
        this.commandsQueue = commands;
        this.optimizedCommandsQueue = optimizedCommandsQueue;
    }

    private boolean tryToCombineCommands() {
        stackTop = commandsStack.peek();
        if (cmd instanceof Read) {
            return false;
        }
        if (cmd instanceof Goto && stackTop instanceof Goto) {
            if ((cmd.getValue() > 0 && stackTop.getValue() > 0) ||
                    (cmd.getValue() < 0 && stackTop.getValue() < 0)) {
                return false;
            }
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
//        for (AbstractCommand com : optimizedCommandsQueue) {
//            System.out.println(com);
//        }
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
            if (command2 instanceof Add && (command2.getValue() == -1 || command2.getValue() == 1) ||
                    (command2 instanceof Assign && command2.getValue() == 0)) {

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

    private void checkBrackets() throws IOException {
        if (cmd instanceof Goto) {
            if (cmd.getValue() > 0) {
                brackets++;
            } else {
                if (brackets-- == 0) {
                    throw new IOException("Brackets mismatch");
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
                    if (brackets != 0) {
                        throw new IOException("Brackets mismatch");
                    }
                    commandsStack.push(cmd);
//                    System.out.println("optimized");
                    flushStackToExecution();
                    return;
                } else {
                    tryToSendLastCommandToExecution();
                }

                checkBrackets();

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
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
