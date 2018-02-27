package com.epam.interpreter;

import java.io.IOException;
import java.util.*;

public class SimpleController implements BFController {

    private BFModel model;
    private BFView view;
    private int bufptr;
    private boolean limited;
    private int currentSize;
    private int bracketsControl;
    private boolean loopNow;
    private boolean skipLoop;
    private int skipBrackets;
    private char symbol;
    private boolean enableTrace;
    private LinkedList<Character> stack = new LinkedList<>();
    private ListIterator<Character> stackIter;

    private final int APPEND_NUMBER = 10;

    public SimpleController(BFModel model, BFView view, boolean limited, boolean enableTrace) throws IOException {
        this.model = model;
        this.view = view;
        this.limited = limited;
        this.enableTrace = enableTrace;
        currentSize = model.getBufSize();
    }

    private boolean getSymbolOrSkip() throws IOException {
        if (!loopNow) {
            symbol = view.readSymbol();
            if (skipLoop) {
                if (symbol != ']') {
                    if (symbol == '[') {
                        bracketsControl++;
                    }
                    stack.push(symbol);
                    return false;
                }
                skipLoop = false;
            }
        } else {
            if (!stackIter.hasPrevious()) {
                loopNow = false;
                return false;
            }
            symbol = stackIter.previous();
            if (skipLoop) {
                if (symbol == '[') {
                    skipBrackets++;
                    bracketsControl++;
                }
                if (symbol == ']' && --skipBrackets == 0) {
                    skipLoop = false;
                    if (--bracketsControl == 0) {
                        loopNow = false;
                    }
                }
                return false;
            }
        }
        return true;
    }

    private void moveLeft() throws IOException {
        if (!limited) {
            if (bufptr == 0) {
                throw new IOException("ptr<0");
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
    }

    private void moveRight() throws IOException {
        if (!limited) {
            if (bufptr == Integer.MAX_VALUE) {
                throw new IOException("max array size");
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
    }

    private int processSymbol() throws IOException {

        if (!getSymbolOrSkip()) {
            return 0;
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
                moveLeft();
                break;
            case '>':
                moveRight();
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
                }
                break;
            case ']':
                if (!loopNow) {
                    stack.push(symbol);
                }
                if (--bracketsControl < 0) {
                    throw new IOException("brackets mismatch");
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

        if (enableTrace) {
            view.printTrace(model.getAllCells(), bufptr);
        }
        return 0;
    }

    private void findLoopStart() {
        int level = 0;
        char sym;

        if (!loopNow) {
            stackIter = stack.listIterator();
        }

        while (stackIter.hasNext()) {
            sym = stackIter.next();
            if (sym == ']') {
                level++;
            }
            if (sym == '[' && (--level) == 0) {
                break;
            }
        }

    }

    @Override
    public void interpret() throws IOException {
        while (processSymbol() != -1) ;
    }
}
