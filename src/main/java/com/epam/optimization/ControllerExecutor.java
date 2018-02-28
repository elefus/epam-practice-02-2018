package com.epam.optimization;

import com.epam.interpreter.BFModel;
import com.epam.interpreter.BFView;
import com.epam.optimization.commands.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;

public class ControllerExecutor implements Runnable {

    private BlockingQueue<Command> optimizedQueue;
    private LinkedList<Command> stack = new LinkedList<>();
    private int stackptr;
    private BFModel model;
    private final BFView view;
    private Command curCmd;
    private boolean limited;
    private boolean loopNow;
    private int bracketsControl;
    private int level;
    private boolean skipLoop;
    private int currentSize;
    private boolean enableTrace;
    private int bufptr;

    private final int APPEND_NUMBER = 10;

    public ControllerExecutor(BlockingQueue<Command> optimizedQueue, BFModel model, BFView view,
                              boolean limited, boolean enableTrace) {
        this.optimizedQueue = optimizedQueue;
        this.model = model;
        this.view = view;
        this.limited = limited;
        this.enableTrace = enableTrace;
        currentSize = model.getBufSize();
    }

    private void processIfAdd() {
        if (!(curCmd instanceof Add)) {
            return;
        }
        model.addCell(bufptr, (byte) curCmd.getVal());
    }

    private void moveLeft() throws IOException {
        if (!limited) {
            if (bufptr + curCmd.getVal() < 0) {
                throw new IOException("ptr<0");
            } else {
                bufptr += curCmd.getVal();
            }
        } else {
            if (bufptr + curCmd.getVal() < 0) {
                bufptr = currentSize + (bufptr + curCmd.getVal());
            } else {
                bufptr -= curCmd.getVal();
            }
        }
    }

    private void moveRight() throws IOException {
        if (!limited) {
            if (bufptr + curCmd.getVal() < bufptr) {
                throw new IOException("max array size");
            } else {
                if (bufptr + curCmd.getVal() >= currentSize - 1) {
                    int newCells = APPEND_NUMBER;
                    if (Integer.MAX_VALUE - currentSize < APPEND_NUMBER) {
                        newCells = Integer.MAX_VALUE - currentSize;
                    }
                    model.increaseBuffer(newCells);
                    currentSize = model.getBufSize();
                }
                bufptr += curCmd.getVal();
            }
        } else {
            if (bufptr + curCmd.getVal() >= currentSize - 1) {
                bufptr = (bufptr + curCmd.getVal()) % currentSize;
            } else {
                bufptr += bufptr + curCmd.getVal();
            }
        }
    }

    private void processIfMove() throws IOException {
        if (!(curCmd instanceof Move)) {
            return;
        }
        if (curCmd.getVal() < 0) {
            moveLeft();
        } else {
            moveRight();
        }
    }

    private void processIfPrint() throws IOException {
        if (!(curCmd instanceof Print)) {
            return;
        }
        for (int i = 0; i < curCmd.getVal(); i++) {
            view.printSymbol((char) model.getCell(bufptr));
        }
    }

    private boolean processIfEnd() {
        return curCmd instanceof End;
    }

    private void processIfRead() throws IOException {
        if (!(curCmd instanceof Read)) {
            return;
        }
        model.setCell(bufptr, (byte) curCmd.getVal());
    }

    private void processIfAssign() {
        if (!(curCmd instanceof Assign)) {
            return;
        }
        model.setCell(bufptr, (byte) curCmd.getVal());
    }

    private void processCloseBracket() {
        bracketsControl--;
        if (!loopNow) {
            stack.push(curCmd);
            stackptr = 0;
        }
        loopNow = true;
        if (skipLoop) {
            skipLoop = false;
            if (stackptr == 0) {
                loopNow = false;
            }
            return;
        }
        stackptr -= curCmd.getVal() - 1;
    }

    private void processOpenBracket() {
        bracketsControl++;
        if (model.getCell(bufptr) == 0) {
            skipLoop = true;
            level = curCmd.getVal();
            stackptr -= (curCmd.getVal() - 1);
        }
    }

    private void processIfGoto() {
        if (!(curCmd instanceof Goto)) {
            return;
        }
        if (curCmd.getVal() < 0) {
            processCloseBracket();
        } else {
            processOpenBracket();
        }
    }

    private int readCmdOrSkip() throws InterruptedException {
        if (!loopNow) {
            curCmd = optimizedQueue.take();
            if (skipLoop) {
                if (level-- == 0) {
                    skipLoop = false;
                } else {
                    stack.push(curCmd);
                    return -1;
                }
            }
        } else {
            curCmd = stack.get(stackptr == 0 ? stackptr : --stackptr);
        }
        return 0;
    }

    @Override
    public void run() {
        while (true) {
            try {

                if (readCmdOrSkip() == -1) {
                    continue;
                }

                if (processIfEnd()) {
                    return;
                }
                processIfAdd();
                processIfMove();
                processIfPrint();
                processIfRead();
                processIfAssign();
                processIfGoto();

                if (bracketsControl > 0 && !loopNow && !(curCmd instanceof Goto && curCmd.getVal() < 0)) {
                    stack.push(curCmd);
                }

                if (enableTrace) {
                    view.printTrace(model.getAllCells(), bufptr);
                }

            } catch (InterruptedException | IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
