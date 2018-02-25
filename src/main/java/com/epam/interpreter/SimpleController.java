package com.epam.interpreter;

import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.*;

public class SimpleController implements BFController {

    private SimpleModel model;
    private SimpleView view;
    private CommandLine cmd;
    private int bufptr;
    private boolean limited;
    private int currentSize;
    private int bracketsControl;
    private boolean loopNow;
    private boolean skipLoop;
    private int skipBrackets;
    private boolean loopStartFound;
    private int loopLevel;
    private LinkedList<Character> stack;
    private ListIterator<Character> stackIter;
    private ListIterator<Character> stackReverseIter;
    //++[>+[-]<-]

    private final int APPEND_NUMBER = 10;

    public SimpleController(CommandLine cmd) throws IOException {
        this.cmd = cmd;
        bufptr = 0;
        loopStartFound = false;
        bracketsControl = 0;
        loopNow = false;
        skipLoop = false;
        loopLevel = 0;
        stack = new LinkedList<>();
        initView();
        initModel();
    }

    private void initView() throws IOException {
        String sourceFile;
        String destFile;

        if (cmd.hasOption("source")) {
            sourceFile = cmd.getOptionValue("source");
        } else {
            sourceFile = null;
        }

        if (cmd.hasOption("out")) {
            destFile = cmd.getOptionValue("out");
        } else {
            destFile = null;
        }

        view = new SimpleView(sourceFile, destFile);
    }

    private void initModel() {
        if (cmd.hasOption("buffer")) {
            int size = Integer.parseInt(cmd.getOptionValue("buffer"));
            model = new SimpleModel(size);
            limited = true;
        } else {
            model = new SimpleModel();
            limited = false;
        }
        currentSize = model.getBufSize();
    }

    private int processSymbol() throws IOException {
        char symbol;

        if (!loopNow) {
            symbol = view.readSymbol();
            if (skipLoop) {
                if (symbol != ']') {
                    if (symbol == '[') {
                        bracketsControl++;
                    }
                    stack.push(symbol);
                    return 0;
                }
                skipLoop = false;
            }
        } else {
            if (!stackIter.hasPrevious()) {
                loopNow = false;
                return 1;
            }
            symbol = stackIter.previous();
            if (skipLoop) {
                if (symbol == '[') {
                    skipBrackets++;
                }
                if (symbol == ']' && --skipBrackets == 0) {
                    skipLoop = false;
                    if (--bracketsControl == 0) {
                        loopNow = false;
                    }
                }
                return 0;
            }
        }

        if (symbol == Character.MAX_VALUE) {
            return -1;
        }


        switch (symbol) {
            case '+':
                model.incrementCell(bufptr);
                break;
            case '-':
                model.decrementCell(bufptr);
                break;
            case '<':
                if (!limited) {
                    if (bufptr == 0) {
                        System.out.println("ptr<0");
                        //exeption
                        break;
                    } else {
                        bufptr--;
                    }
                } else {
                    if (bufptr == 0) {
                        bufptr = currentSize - 1;
                    } else {
                        bufptr--;
                    }
                }
                break;
            case '>':
                if (!limited) {
                    if (bufptr == Integer.MAX_VALUE) {
                        //exeption
                        break;
                    } else {
                        if (bufptr == currentSize - 1) {
                            int newCells = APPEND_NUMBER;
                            if (Integer.MAX_VALUE - currentSize < APPEND_NUMBER) {
                                newCells = Integer.MAX_VALUE - currentSize;
                            }
                            model.increaseBuffer(newCells);
                            currentSize = model.getBufSize();
                        }
                        bufptr++;
                    }
                } else {
                    if (bufptr == currentSize - 1) {
                        bufptr = 0;
                    } else {
                        bufptr++;
                    }
                }
                break;
            case '.':
                view.printSymbol((char) model.getCell(bufptr));
                break;
            case ',':
                char input = view.readInput();
                model.setCell(bufptr, (byte) input);
                break;
            case '[':
                bracketsControl++;
                if (model.getCell(bufptr) == 0) {
                    skipLoop = true;
                    skipBrackets = 1;
                    loopStartFound = false;
                }
                break;
            case ']':
                if (!loopNow) {
                    stack.push(symbol);
                }
                if (--bracketsControl < 0) {
                    //exeption
                    System.out.println("brackets mismatch");
                }
                findLoopStart();
                loopNow = true;
                break;
            default:
                return 1;
        }

        if (bracketsControl > 0 && !loopNow) {
            stack.push(symbol);
        }

        view.printTrace(model.getAllCells(), bufptr);
        return 0;
    }

    private void findLoopStart() {
        int level = 0;
        char symbol;
//        if (loopStartFound) {
//            stackReverseIter = stackIter;
//            return;
//        }
        //if (stackIter == null) {
        stackIter = stack.listIterator();
        // }
        while (stackIter.hasNext()) {
            symbol = stackIter.next();
            if (symbol == ']') {
                level++;
            }
            if (symbol == '[' && (--level) - bracketsControl <= 0) {
//                stackReverseIter = stackIter;
                break;
            }
        }
    }

    @Override
    public void interpret() throws IOException {
        while (processSymbol() != -1) ;
    }
}
