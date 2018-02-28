package com.epam.optimizedInterpreter;

import com.epam.interpreter.BfModel;
import com.epam.interpreter.BfView;

import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;

public class CommandExecutor implements Runnable {
    private static final int CELLS_TO_ADD = 10;
    private BfModel model;
    private BfView view;
    private boolean bufferIsInfinite;
    private boolean traceMode;
    private int bufferSize;
    private BlockingQueue<AbstractCommand> commandsQueue;
    private AbstractCommand cmd;
    private int pointer;
    private boolean skippingProcess;
    private boolean isLoop;
    private int stackPointer;
    private int brackets;
    private int commandsToSkip;
    private LinkedList<AbstractCommand> commandStack = new LinkedList<>();

    public CommandExecutor(BfModel model, BfView view, BlockingQueue<AbstractCommand> commandsQueue,
                           boolean bufferIsInfinite, boolean traceMode, int bufferSize) {
        this.model = model;
        this.view = view;
        this.commandsQueue = commandsQueue;
        this.bufferIsInfinite = bufferIsInfinite;
        this.traceMode = traceMode;
        this.bufferSize = bufferSize;
    }

    private boolean executeAdd() {
        if (!(cmd instanceof Add)) {
            return false;
        }
        model.addValue(pointer, (byte) cmd.getValue());
        return true;
    }

    private boolean executeMove() throws IOException {
        if (!(cmd instanceof Move)) {
            return false;
        }
        int value = cmd.getValue();
        if (value < 0) {
            if (bufferIsInfinite) {
                if (pointer - value < 0) {
                    throw new IOException("pointer < 0");
                } else {
                    pointer -= -value;
                }
            } else {
                if (pointer - value < 0) {
                    int step = bufferSize - (-(pointer - value));
                    pointer = step;
                } else {
                    pointer -= value;
                }
            }
        } else {
            if (bufferIsInfinite) {
                if (pointer + value == bufferSize) {
                    if (bufferSize + CELLS_TO_ADD < Integer.MAX_VALUE) {
                        model.changeBufferSize(CELLS_TO_ADD);
                        pointer += value;
                        bufferSize += CELLS_TO_ADD;
                    } else {
                        throw new IOException("pointer > 0");
                    }
                } else {
                    pointer += value;
                }
            } else {
                if (pointer + value >= bufferSize) {
                    pointer = value - (bufferSize - pointer);
                } else {
                    pointer++;
                }
            }
        }
        return true;
    }

    private boolean executePrint() throws IOException {
        if (!(cmd instanceof Print)) {
            return false;
        }
        char res = (char) model.getValue(pointer);
        for (int i = 0; i < cmd.getValue(); i++) {
            view.print(res);
        }
        return true;
    }

    private boolean executeRead() {
        if (!(cmd instanceof Read)) {
            return false;
        }
        int value = cmd.getValue();
        model.setValue((byte) value, pointer);
        return true;
    }

    private boolean executeGoto() {
        if (!(cmd instanceof Goto)) {
            return false;
        }
        int cellValue;
        int step = cmd.getValue();
        if (step > 0) {
            brackets++;
            cellValue = model.getValue(pointer);
            if (cellValue == 0) {
                skippingProcess = true;
                stackPointer -= step - 1;
                commandsToSkip = step;
            }
        } else {
            brackets--;
            if (!isLoop) {
                commandStack.push(cmd);
                stackPointer = 0;
            }
            isLoop = true;
            if (skippingProcess) {
                skippingProcess = false;
                if (stackPointer == 0) {
                    isLoop = false;
                }
            } else {
                stackPointer -= step - 1;
            }
        }
        return true;
    }

    private boolean executeAssign() {
        if (!(cmd instanceof Assign)) {
            return false;
        }
        model.setValue(((byte) cmd.getValue()), pointer);
        return true;
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (!isLoop) {
                    cmd = commandsQueue.take();
                    if(skippingProcess){
                        commandsToSkip--;
                        if(commandsToSkip==0){
                            skippingProcess = false;
                        }else{
                            commandStack.push(cmd);
                            continue;
                        }
                    }
                } else {
                    cmd = commandStack.get(stackPointer == 0 ? stackPointer : --stackPointer);
                }

                if (cmd instanceof End) {
                    return;
                }

                System.out.println(cmd);

                executeAdd();
                executeMove();
                executeAssign();
                executePrint();
                executeRead();
                executeGoto();

                if (brackets > 0 && !isLoop &&
                        !(cmd instanceof Goto && cmd.getValue() < 0)) {
                    commandStack.push(cmd);
                }

                if (traceMode) {
                    view.trace(model.getAllBuffer(), pointer);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
