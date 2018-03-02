package com.epam.optimization;

import com.epam.optimization.commands.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;

public class ControllerOptimizer implements Runnable {
    private BlockingQueue<Command> inputQueue;
    private BlockingQueue<Command> optimizedQueue;
    private Command curCmd;
    private Command stackCmd;
    private Command optimizedCmd;
    private LinkedList<Command> stack = new LinkedList<>();
    private Iterator<Command> stackIter;
    private final int STACK_SIZE = 100;
    private int bracketsControl;

    public ControllerOptimizer(BlockingQueue<Command> inputQueue, BlockingQueue<Command> optimizedQueue) {
        this.inputQueue = inputQueue;
        this.optimizedQueue = optimizedQueue;
    }

    private boolean isEnd() throws InterruptedException, IOException {
        if (curCmd instanceof End) {
            if (bracketsControl != 0) {
                throw new IOException("brackets mismatch " + bracketsControl);
            }
            while ((optimizedCmd = stack.pollLast()) != null) {
                optimizedQueue.put(optimizedCmd);
            }
            optimizedQueue.put(curCmd);
//            System.out.println("OPTIMIZED");
            return true;
        }
        return false;
    }

    private int checkStackOverflow() throws InterruptedException {
        if (stack.isEmpty()) {
            stack.push(curCmd);
            return -1;
        }

        if (stack.size() >= STACK_SIZE) {
            if (stackCmd instanceof Goto && stackCmd.getVal() == 1) {
                return 0;
            }
            optimizedCmd = stack.pollLast();
            optimizedQueue.put(optimizedCmd);
        }
        return 0;
    }

    private int tryToMergeCommands() {
        if (curCmd.getClass() == Goto.class && curCmd.getVal() == 1) {
            return 0;
        }
        if (curCmd.getClass() != Read.class && curCmd.getClass() == stackCmd.getClass()) {
            stack.pop();
            int newVal = curCmd.getVal() + stackCmd.getVal();
            if (newVal != 0) {
                curCmd.setVal(newVal);
                stack.push(curCmd);
            }
            return -1;
        }
        if (curCmd.getClass() == Add.class && stack.peek().getClass() == Assign.class) {
            Command c = stack.pop();
            stack.push(new Assign(c.getVal() + curCmd.getVal()));
            return -1;
        }
        return 0;
    }

    private int findAndSetLoopStart() {
        stack.push(curCmd);
        stackIter = stack.listIterator();
        Command itCmd;
        int offset = 0;
        int level = 0;
        while (stackIter.hasNext()) {
            itCmd = stackIter.next();
            if (itCmd instanceof Goto) {
                if (itCmd.getVal() < 0) {
                    level++;
                } else if (--level == 0) {
                    itCmd.setVal(offset);
                    stack.pop();
                    stack.push(new Goto(-offset));
                    if (offset == 2) {
                        tryChangeForAssign();
                    }
                    return -1;
                }
            }
            offset++;
        }
        return 0;
    }

    private void tryChangeForAssign() {
        stackIter = stack.listIterator();
        Command itCmd;
        if (!stackIter.hasNext()) {
            return;
        }
        stackIter.next();
        if (!stackIter.hasNext()) {
            return;
        }
        itCmd = stackIter.next();
        if ((itCmd instanceof Add && (itCmd.getVal() == 1 || itCmd.getVal() == -1))
                || (itCmd instanceof Assign && itCmd.getVal() == 0)) {
            for (int i = 0; i < 3; i++) {
                stack.pop();
            }
            deleteAddBeforeAssign();
            stack.push(new Assign(0));
        }
    }

    private void deleteAddBeforeAssign() {
        if (stack.isEmpty()) {
            return;
        }
        if (stack.peek().getClass() == Add.class) {
            stack.pop();
        }
    }

    private void checkBracketsMatch() throws IOException {
        if (curCmd.getClass() == Goto.class) {
            if (curCmd.getVal() > 0) {
                bracketsControl++;
                return;
            }
            if (--bracketsControl < 0) {
                throw new IOException("brackets mismatch");
            }
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                curCmd = inputQueue.take();

//                System.out.println(curCmd.toString());
                stackCmd = stack.peek();

                if (isEnd()) {
                    return;
                }

                checkBracketsMatch();

                if (checkStackOverflow() == -1) {
                    continue;
                }

                if (tryToMergeCommands() == -1) {
                    continue;
                }

                if (curCmd instanceof Goto && curCmd.getVal() == -1) {
                    if (findAndSetLoopStart() == -1) {
                        continue;
                    }
                }

                stack.push(curCmd);
            }
        } catch (InterruptedException | IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
