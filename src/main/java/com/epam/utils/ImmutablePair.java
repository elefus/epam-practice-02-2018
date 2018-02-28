package com.epam.utils;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class ImmutablePair<T, V> {
    private final T first;
    private final V second;

    public ImmutablePair(T first, V second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public V getSecond() {
        return second;
    }

    @NotNull
    public <R> ImmutablePair<R, V> mapFirst(@NotNull Function<T, R> mapper) {
        return new ImmutablePair<>(mapper.apply(first), second);
    }

    @NotNull
    public <R> ImmutablePair<T, R> mapSecond(@NotNull Function<V, R> mapper) {
        return new ImmutablePair<>(first, mapper.apply(second));
    }

    @NotNull
    public <R, U> ImmutablePair<R, U> mapBoth(@NotNull Function<T, R> mapperFirst, @NotNull Function<V, U> mapperSecond) {
        return new ImmutablePair<>(mapperFirst.apply(first), mapperSecond.apply(second));
    }
}
