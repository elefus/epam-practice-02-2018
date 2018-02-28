package com.epam.interpreter;

import org.apache.commons.cli.CommandLine;

import java.io.IOException;
import java.util.LinkedList;
import java.util.ListIterator;


public class ControllerImpl implements BfController {

    private static final int CELLS_TO_ADD = 10;

    private BfView view;
    private BfModel model;
    private int bufferSize;
    private int pointer;
    private boolean bufferIsInfinite;
    private boolean isLoop;
    private boolean endLoop;
    private LinkedList<Character> loopsStack = new LinkedList<>();
    private int brackets;
    private char input;
    private ListIterator<Character> commandIterator;
    private int loopStage;
    private char symbol;
    private int bracketsToSkip;
    private boolean enableTraceMode;

    public ControllerImpl(BfView view, BfModel model, boolean isInfinite,
                          int bufferSize, boolean enableTraceMode) {
        this.view = view;
        this.model = model;
        this.bufferIsInfinite = isInfinite;
        this.bufferSize = bufferSize;
        this.enableTraceMode = enableTraceMode;
    }

    @Override
    public void interpret() throws IOException {
        while (interpretNextSymbol() != -1) ;
    }

    private boolean getSymbolOrSkip() throws IOException {
        if (!isLoop) {
            symbol = view.readSymbol();
            if (endLoop) {
                if (symbol != ']') {
                    if (symbol == '[') {
                        brackets++;
                    }
                    loopsStack.push(symbol);
                    return false;
                }
                endLoop = false;
            }
        } else {
            if (commandIterator.hasPrevious()) {
                symbol = commandIterator.previous();
                if (endLoop) {
                    if (symbol == ']') {
                        brackets--;
                    }
                    if (symbol == '[') {
                        brackets++;
                    }
                    if (symbol == ']' && brackets == bracketsToSkip) {
                        if (commandIterator.hasPrevious()) {
                            isLoop = true;
                        } else {
                            isLoop = false;
                        }
                        endLoop = false;
                        return false;
                    }
                    return false;
                }
            } else {
                if (endLoop) {
                    endLoop = false;
                    if (brackets == 0) {
                        isLoop = false;
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private int interpretNextSymbol() throws IOException {

        if (!getSymbolOrSkip()) {
            return 0;
        }

        if (symbol == Character.MAX_VALUE) {
            return -1;
        }

        switch (symbol) {
            case '+':
                model.addValue(pointer, (byte) 1);
                break;
            case '-':
                model.addValue(pointer, (byte) -1);
                break;
            case '>':
                if (bufferIsInfinite) {
                    if (pointer + 1 == bufferSize) {
                        if (bufferSize + CELLS_TO_ADD < Integer.MAX_VALUE) {
                            model.changeBufferSize(CELLS_TO_ADD);
                            pointer++;
                            bufferSize += CELLS_TO_ADD;
                        } else {
                            System.out.println("buffer > MAX_INT");
                        }
                    } else {
                        pointer++;
                    }
                } else {
                    if (pointer + 1 == bufferSize) {
                        pointer = 0;
                    } else {
                        pointer++;
                    }
                }
                break;
            case '<':
                if (bufferIsInfinite) {
                    if (pointer - 1 < 0) {
                        System.out.println("pointer < 0");
                    } else {
                        pointer--;
                    }
                } else {
                    if (pointer - 1 < 0) {
                        pointer = bufferSize - 1;
                    } else {
                        pointer--;
                    }
                }
                break;
            case '[':
                brackets++;
                if (model.getValue(pointer) == 0) {
                    endLoop = true;
                    bracketsToSkip = brackets - 1;
                }
                break;
            case ']':
                if (!isLoop) {
                    loopsStack.push(symbol);
                }
                loopStage = 0;
                brackets--;
                endLoop = false;
                if (!isLoop) {
                    commandIterator = loopsStack.listIterator();
                }
                isLoop = true;

                while (commandIterator.hasNext()) {
                    char stackCommand = commandIterator.next();
                    if (stackCommand == '[') {
                        loopStage--;
                    }
                    if (stackCommand == '[' && loopStage == 0) {
                        break;
                    }
                    if (stackCommand == ']') {
                        loopStage++;
                    }
                }
                break;
            case '.':
                char res = (char) model.getValue(pointer);
                view.print(res);
                break;
            case ',':
                input = view.readInput();
                model.setValue((byte) input, pointer);
                break;
            default:
                return 0;
        }

        if (brackets > 0 && !isLoop) {
            loopsStack.push(symbol);
        }

        if (enableTraceMode) {
            view.trace(model.getAllBuffer(), pointer);
        }
        return 0;
    }
}
