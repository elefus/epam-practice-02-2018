package com.epam.utils.math;

public class RingFactory {
    public static Ring getRing(int value, Integer... arguments) throws UnsupportedOperationException {
        if (arguments.length > 1) {
            throw new UnsupportedOperationException("Can't be called with more than one argument");
        }
        return (arguments.length == 1) ? new FiniteRing(value, arguments[0]) : new InfiniteRing(value);
    }
}
