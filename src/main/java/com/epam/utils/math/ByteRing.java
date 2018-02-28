package com.epam.utils.math;

import org.jetbrains.annotations.NotNull;

public class ByteRing extends FiniteRing {
    @Deprecated
    ByteRing(int rawValue, int module) {
        super(rawValue, module);
    }

    public ByteRing(int rawValue) {
        super(rawValue, 256);
    }

    @NotNull
    public ByteRing increment(int rawValue) {
        return new ByteRing(rawValue + this.getValue());
    }
}
