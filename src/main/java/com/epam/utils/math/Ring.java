package com.epam.utils.math;

import org.jetbrains.annotations.NotNull;

public interface Ring {
    int getValue();

    @NotNull Ring increment(int value);
}
