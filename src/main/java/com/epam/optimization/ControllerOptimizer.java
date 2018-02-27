package com.epam.optimization;

import com.epam.optimization.commands.*;

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

    public ControllerOptimizer(BlockingQueue<Command> inputQueue, BlockingQueue<Command> optimizedQueue) {
        this.inputQueue = inputQueue;
        this.optimizedQueue = optimizedQueue;
    }

    private boolean isEnd() throws InterruptedException {
        if (curCmd instanceof End) {
            while ((optimizedCmd = stack.pollLast()) != null) {
                optimizedQueue.put(optimizedCmd);
            }

            System.out.println("OPTIMIZED");
            for (Command c : optimizedQueue) {
                System.out.println(c);
            }
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
        if (itCmd instanceof Add && (itCmd.getVal() == 1 || itCmd.getVal() == -1)) {
            for (int i = 0; i < 3; i++) {
                stack.pop();
            }
            deleteAddBeforeAssign();
            stack.push(new Assign(0));
        }
    }

    private void deleteAddBeforeAssign() {
        if (stack.peek().getClass() == Add.class) {
            stack.pop();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                curCmd = inputQueue.take();

                System.out.println(curCmd.toString());
                stackCmd = stack.peek();

                if (isEnd()) {
                    return;
                }

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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
