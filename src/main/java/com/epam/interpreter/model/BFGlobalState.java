package com.epam.interpreter.model;

import com.epam.interpreter.base.BrainfuckInstruction;
import com.epam.utils.math.ByteRing;
import com.epam.utils.math.Ring;
import com.epam.utils.math.RingFactory;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toCollection;

public class BFGlobalState {
    private final ArrayList<ByteRing> cells;
    private final BufferedReader input;
    private final BufferedWriter output;
    @NotNull
    private Ring focus;
    @NotNull
    private final ArrayList<BrainfuckInstruction> instructions;
    @NotNull
    private Ring instructionFocus;

    public BFGlobalState(ArrayList<Byte> cells, int maxSize, BufferedReader input, BufferedWriter output) {
        this.cells = cells
                .stream()
                .map(ByteRing::new)
                .collect(toCollection(ArrayList::new));
        this.input = input;
        this.output = output;
        this.instructions = new ArrayList<>();
        this.focus = maxSize > 0 ? RingFactory.getRing(0, maxSize) : RingFactory.getRing(0);
        this.instructionFocus = RingFactory.getRing(0);
    }

    public BFGlobalState(int maxSize, BufferedReader input, BufferedWriter output) {
        this.input = input;
        this.output = output;
        this.focus = maxSize > 0 ? RingFactory.getRing(0, maxSize) : RingFactory.getRing(0);
        this.cells = maxSize < 1
                ? new ArrayList<>()
                : IntStream
                .range(0, maxSize)
                .mapToObj(i -> new ByteRing(0))
                .collect(toCollection(ArrayList::new));
        this.instructions = new ArrayList<>();
        this.instructionFocus = RingFactory.getRing(0);
    }

    public ArrayList<Byte> getCells() {
        return cells
                .stream()
                .map(ByteRing::getValue)
                .map(Integer::byteValue)
                .collect(toCollection(ArrayList::new));
    }

    public BufferedReader getInput() {
        return input;
    }

    public BufferedWriter getOutput() {
        return output;
    }

    @NotNull
    public Ring getFocus() {
        return focus;
    }

    @NotNull
    public ArrayList<BrainfuckInstruction> getInstructions() {
        return instructions;
    }

    @NotNull
    public BFGlobalState add(int value) {
        this.cells.set(this.focus.getValue(), this.cells.get(this.focus.getValue()).increment(value));
        return this;
    }

    @NotNull
    public BFGlobalState move(int value) {
        this.focus = this.focus.increment(value);
        return this;
    }

    @NotNull
    public BFGlobalState assign(int value) {
        this.cells.set(this.focus.getValue(), new ByteRing(value));
        return this;
    }

    @NotNull
    public BFGlobalState execute() {
        this.instructionFocus = this.instructionFocus.increment(1);
        return this;
    }

    @NotNull
    public BFGlobalState addInstruction(BrainfuckInstruction instruction) {
        this.instructions.add(instruction);
        return this;
    }

    @NotNull
    public BFGlobalState addInstructions(Collection<BrainfuckInstruction> instructions) {
        this.instructions.addAll(instructions);
        return this;
    }

    public BrainfuckInstruction jmp(int value) {
        this.instructionFocus = this.instructionFocus.increment(value);
        return instructions.get(this.instructionFocus.getValue());
    }
}
