package com.epam.interpreter;

import org.apache.commons.cli.CommandLine;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;


public class ControllerImpl implements BfController {

    private static final int CELLS_TO_ADD = 10;

    private ViewImpl view;
    private ModelImpl model;
    private String sourceFile;
    private String outputFile;
    private int bufferSize;
    private int pointer;
    private boolean bufferIsInfinite;
    private boolean isLoop;
    private boolean endLoop;
    private LinkedList<Character> loopsStack;
    private int brackets;
    private char input;
    private static final int DEFAULT_BUFFER_SIZE = 10;
    private ListIterator<Character> commandIterator;
    private int loopStage;
    private int countCommands;
    private char symbol;
    private char cmd;
    private int bracketsToSkip;

    private void initViewModel(String[] args) throws IOException {
        CommandLine cmd;
        cmd = ArgsParser.parseArgs(args);

        if (cmd.hasOption("source")) {
            sourceFile = cmd.getOptionValue("source");
        } else {
            sourceFile = null;
        }

        if (cmd.hasOption("out")) {
            outputFile = cmd.getOptionValue("out");
        } else {
            outputFile = null;
        }

        if (sourceFile == null && outputFile == null) {
            view = new ViewImpl(System.in, System.out);     //Console=>Console
        } else if (sourceFile != null && outputFile != null) {
            view = new ViewImpl(sourceFile, outputFile);    //File=>File
        } else if (sourceFile == null && outputFile != null) {
            view = new ViewImpl(System.in, outputFile);     //Console=>File
        } else if (sourceFile != null && outputFile == null) {
            view = new ViewImpl(sourceFile, System.out);    //File=>Console
        }

        if (cmd.hasOption("buffer")) {
            bufferSize = Integer.parseInt(cmd.getOptionValue("buffer"));
            model = new ModelImpl(bufferSize);
            bufferIsInfinite = false;
        } else {
            bufferSize = DEFAULT_BUFFER_SIZE;
            model = new ModelImpl();
            bufferIsInfinite = true;
        }
    }

    public ControllerImpl(String[] args) throws IOException {
        initViewModel(args);
        pointer = 0;
        loopsStack = new LinkedList<>();
        isLoop = false;
        brackets = 0;
        endLoop = false;
        loopStage = 0;
        bracketsToSkip = 0;
    }

    @Override
    public void interpret() throws IOException {
        while (interpretNextSymbol() != -1) ;
    }

    private int interpretNextSymbol() throws IOException {

        if (!isLoop) {
            symbol = view.readSymbol();
            if(endLoop){
                if(symbol!=']'){
                    if(symbol=='['){
                        brackets++;
                    }
                    loopsStack.push(symbol);
                    return 0;
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
                        return 0;
                    }
                    return 0;
                }
            } else {
                if (endLoop) {
                    endLoop = false;
                    if (brackets == 0) {
                        isLoop = false;
                    }
                    return 0;
                }
            }
        }

        if (symbol == Character.MAX_VALUE) {
            return -1;
        }

        switch (symbol) {
            case '+':
                model.increment(pointer);
                break;
            case '-':
                model.decrement(pointer);
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
                isLoop = true;
                commandIterator = loopsStack.listIterator();

                countCommands = 0;
                while (commandIterator.hasNext()) {
                    char stackCommand = commandIterator.next();

                    countCommands++;
                    if (stackCommand == '[') {
                        loopStage--;
                    }
                    if (stackCommand == '[' && loopStage - brackets <= 0) {
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

        view.trace(model.getAllBuffer(), pointer);
        return 0;
    }
}
