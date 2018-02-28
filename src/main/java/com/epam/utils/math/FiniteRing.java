package com.epam.utils.math;

import org.jetbrains.annotations.NotNull;

public class FiniteRing implements Ring {
    private final int value;
    private final int module;

    FiniteRing(int rawValue, int module) {
        this.module = module;
        int value = rawValue % module;
        this.value = value < 0 ? module + value : value;
    }

    @Override
    public int getValue() {
        return value;
    }

    @NotNull
    @Override
    public FiniteRing increment(int rawValue) {
        return new FiniteRing(this.value + rawValue, this.module);
    }

    @NotNull
    @Override
    public String toString() {
        return value + " by module: " + module;
    }
}
