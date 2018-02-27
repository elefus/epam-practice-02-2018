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
    private final int STACK_SIZE = 10;

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

    private int checkStackSize() throws InterruptedException {
        if (stack.isEmpty()) {
            stack.push(curCmd);
            return -1;
        }

        if (stack.size() == STACK_SIZE) {
            optimizedCmd = stack.pollLast();
            optimizedQueue.put(optimizedCmd);
        }
        return 0;
    }

    private int checkCurAndTopType() {
        if (curCmd.getClass() == stackCmd.getClass()) {
            stack.pop();
            int newVal = curCmd.getVal() + stackCmd.getVal();
            if (newVal != 0) {
                curCmd.setVal(newVal);
                stack.push(curCmd);
            }
            return -1;
        }
        return 0;
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

                if (checkStackSize() == -1) {
                    continue;
                }

                if (checkCurAndTopType() == -1) {
                    continue;
                }

                stack.push(curCmd);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
