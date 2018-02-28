package com.epam.interpreter.controller;

import com.epam.utils.functional.Functional;
import com.epam.utils.functional.MTIFunction;

import java.util.Stack;

public class Optimizations {
    private final Stack<Character> stack;

    Optimizations(Stack<Character> stack) {
        this.stack = stack;
    }

    public Stack<Character> getStack() {
        return stack;
    }

    private static MTIFunction<Character, Boolean> biNegationPredicate(Character symb1, Character symb2) {
        return (Character... args) -> (args[0] == symb1) && (args[1] == symb2);
    }

    private static MTIFunction<Character, Boolean> minusWithPlus() {
        return biNegationPredicate('-', '+');
    }

    private static MTIFunction<Character, Boolean> plusWithMinus() {
        return biNegationPredicate('+', '-');
    }

    private static MTIFunction<Character, Boolean> rightWithLeft() {
        return biNegationPredicate('>', '<');
    }

    private static MTIFunction<Character, Boolean> leftWithRight() {
        return biNegationPredicate('<', '>');
    }

    public Stack<Character> optimize() {
        if (stack.size() < 2) {
            return stack;
        }
        Character last = stack.pop();
        Character preLast = stack.pop();
        if (!Functional.predicateDisjunct(
                minusWithPlus(),
                plusWithMinus(),
                rightWithLeft(),
                leftWithRight()
        ).apply(last, preLast)) {
            stack.push(preLast);
            stack.push(last);
        }
        return stack;
    }
}
