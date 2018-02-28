package com.epam.utils.functional;

import com.epam.utils.ImmutablePair;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Functional {
    public static <T, U, R> Stream<R> zipWith(Stream<T> left, Stream<U> right, @NotNull BiFunction<T, U, R> zipper) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                new Iterator<R>() {
                    @NotNull Iterator<T> itLeft = left.iterator();
                    @NotNull Iterator<U> itRight = right.iterator();

                    @Override
                    public boolean hasNext() {
                        return itLeft.hasNext() && itRight.hasNext();
                    }

                    @Override
                    public R next() {
                        return zipper.apply(itLeft.next(), itRight.next());
                    }
                },
                0),
                false
        );
    }

    public static <T> Stream<ImmutablePair<T, Integer>> index(@NotNull Stream<T> input) {
        return zipWith(input, IntStream.iterate(0, i -> i + 1).boxed(), ImmutablePair::new);
    }

    @SafeVarargs
    public static <T> MTIFunction<T, Boolean> predicateDisjunct(@NotNull MTIFunction<T, Boolean>... preds) {
        return args -> Arrays.stream(preds).reduce(args1 -> false, Functional::or).apply(args);
    }

    private static <T> MTIFunction<T, Boolean> or(@NotNull MTIFunction<T, Boolean> first, @NotNull MTIFunction<T, Boolean> second) {
        return args -> first.apply(args) || second.apply(args);
    }
}
