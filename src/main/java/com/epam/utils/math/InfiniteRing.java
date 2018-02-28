package com.epam.utils.math;

import org.jetbrains.annotations.NotNull;

public class InfiniteRing implements Ring {
    private final int value;

    InfiniteRing(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }

    @NotNull
    @Override
    public Ring increment(int value) {
        return new InfiniteRing(value + this.value);
    }

    @NotNull
    @Override
    public String toString() {
        return value + " by module: ∞";
    }
}
