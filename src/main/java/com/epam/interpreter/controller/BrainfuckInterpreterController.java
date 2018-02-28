package com.epam.interpreter.controller;

import com.epam.interpreter.base.*;
import com.epam.utils.ImmutablePair;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

import static com.epam.interpreter.base.BFInstructions.*;

public class BrainfuckInterpreterController {
    @NotNull
    private final static Set<Character> operations;
    @NotNull
    private final static Map<Character, BFInstructions> operationMatcher;
    @NotNull
    private final static Map<Character, Consumer<AtomicInteger>> operationArgChanger;
    @NotNull
    private final static Map<BFInstructions, BiFunction<BrainfuckInstruction, Integer, BrainfuckInstruction>> instructionMatcher;


    static {
        instructionMatcher = new TreeMap<>();
        instructionMatcher.put(ADD, (bf, i) -> new AddBI(i, bf));
        instructionMatcher.put(MOVE, (bf, i) -> new MoveBI(i, bf));
        instructionMatcher.put(PRINT, (bf, i) -> new PrintBI(i, bf));
        instructionMatcher.put(READ, (bf, i) -> new ReadBI(i, bf));
        instructionMatcher.put(ASSIGN, (bf, i) -> new AssignBI(i, bf));
        instructionMatcher.put(JMP, (bf, i) -> new JmpBI(i, bf));
        instructionMatcher.put(JE, (bf, i) -> new JeBI(i, bf));
        instructionMatcher.put(NOP, (bf, i) -> new NopBI(bf));
        operationArgChanger = new TreeMap<>();
        operationArgChanger.put('+', x -> x.addAndGet(1));
        operationArgChanger.put('-', x -> x.addAndGet(-1));
        operationArgChanger.put('>', x -> x.addAndGet(1));
        operationArgChanger.put('<', x -> x.addAndGet(-1));
        operationArgChanger.put('.', x -> x.addAndGet(1));
        operationArgChanger.put(',', x -> x.addAndGet(1));
        operationArgChanger.put('[', x -> {
        });
        operationArgChanger.put(']', x -> {
        });
        operationArgChanger.put('n', x -> {

        });
        operationMatcher = new TreeMap<>();
        operationMatcher.put('+', ADD);
        operationMatcher.put('-', ADD);
        operationMatcher.put('>', MOVE);
        operationMatcher.put('<', MOVE);
        operationMatcher.put('.', PRINT);
        operationMatcher.put(',', READ);
        operationMatcher.put('[', JE);
        operationMatcher.put(']', JMP);
        operationMatcher.put('n', NOP);
        operations = new TreeSet<>();
        operations.add('+');
        operations.add('-');
        operations.add('>');
        operations.add('<');
        operations.add('.');
        operations.add(',');
        operations.add('[');
        operations.add(']');
        operations.add('n');
    }

    public static void finalize(@NotNull AtomicReference<BFInstructions> currInstruction,
                                AtomicInteger arg,
                                @NotNull LinkedList<ImmutablePair<BFInstructions, Integer>> instructions,
                                BFInstructions newInstruction) {
        if (arg.get() != 0) {
            instructions.add(new ImmutablePair<>(currInstruction.get(), arg.get()));
        }
        arg.set(0);
        currInstruction.set(newInstruction);
    }

    public static BrainfuckInstruction parse(String input) throws IllegalArgumentException {
        @NotNull Optimizations stack = new Optimizations(new Stack<>());
        stack.getStack().push('n');
        @NotNull LinkedList<ImmutablePair<BFInstructions, Integer>> instructions = new LinkedList<>();
        @NotNull AtomicInteger arg = new AtomicInteger();
        @NotNull AtomicReference<BFInstructions> currInstruction = new AtomicReference<>(NOP);
        input = input + 'n';
        input.codePoints().mapToObj(c -> (char) c).filter(operations::contains)
                .forEach(c -> {
                    switch (c) {
                        case ',':
                        case '.':
                        case '>':
                        case '<':
                        case '-':
                        case '+':
                        case 'n': {
                            if ((stack.getStack().size() > 0) && (operationMatcher.get(stack.getStack().peek()) != operationMatcher.get(c))) {
                                finalize(currInstruction, arg, instructions, operationMatcher.get(c));
                            }
                            stack.optimize();
                            operationArgChanger.get(c).accept(arg);
                            break;
                        }
                        case ']':
                        case '[': {
                            finalize(currInstruction, arg, instructions, operationMatcher.get(c));
                            arg.set(-1);
                            break;
                        }

                    }
                    stack.getStack().push(c);
                });

        ArrayList<ImmutablePair<BFInstructions, Integer>> fastInstructions = new ArrayList<>(instructions);
        Stack<Integer> jeIndices = new Stack<>();
        int balance = 0;
        int distance;
        for (int i = 0; i < fastInstructions.size(); ++i) {
            if (fastInstructions.get(i).getFirst() == JE) {
                balance++;
                jeIndices.push(i);
            }
            if (fastInstructions.get(i).getFirst() == JMP) {
                balance--;
                if (balance < 0) {
                    throw new IllegalArgumentException("Missing brackets");
                }
                distance = i - jeIndices.peek();
                fastInstructions.set(i, new ImmutablePair<>(JMP, -distance));
                fastInstructions.set(jeIndices.peek(), new ImmutablePair<>(JE, distance + 1));
                jeIndices.pop();
            }
        }
        if (balance != 0) {
            throw new IllegalArgumentException("Missing brackets");
        }
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize
                (new LinkedList<>(fastInstructions).descendingIterator(), 0), false)
                .reduce((BrainfuckInstruction) new EndBI(),
                        (prevI, pair) -> instructionMatcher.get(pair.getFirst()).apply(prevI, pair.getSecond()),
                        (j, k) -> null);
    }
}
