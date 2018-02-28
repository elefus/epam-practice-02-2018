package com.epam.utils.functional;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface MTIFunction<T, R> {
    @NotNull R apply(T... args);
}
