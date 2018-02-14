package com.epam;


import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Spliterators.spliteratorUnknownSize;

public class Utils {
    @NotNull
    public static <T, U, R> Stream<R> zip(@NotNull Stream<T> tStream,
                                          @NotNull Stream<U> uStream,
                                          @NotNull BiFunction<T, U, R> zipper) {
        return StreamSupport.stream(spliteratorUnknownSize(
                new Iterator<R>() {
                    Iterator<T> itT = tStream.iterator();
                    Iterator<U> itU = uStream.iterator();

                    @Override
                    public boolean hasNext() {
                        return itT.hasNext() && itU.hasNext();
                    }

                    @Override
                    public R next() {
                        return zipper.apply(itT.next(), itU.next());
                    }
                },
                0),
                false
        );
    }

    public static int polynomStringHash(String value, int hashNumber) {
        return zip(value.codePoints().boxed(),
                Stream.iterate(1, i -> i * hashNumber),
                (c, p) -> c * p)
                .reduce(0, (a, b) -> a + b);
    }

    private static int _unsafeFastAccPow(int a, int x, int res) {
        return (x == 1)
                ? res
                : ((x & 1) == 0)
                    ? _unsafeFastAccPow(a, x >> 1, res * res)
                    : _unsafeFastAccPow(a, (x - 1) >> 1, a * res * res);

    }

    public static int unsafeFastPow(int a, int x) {
        return _unsafeFastAccPow(a, x, a);
    }

    public static String fromListToString(List<Integer> input) {
        return input
                .stream()
                .mapToInt(i -> i)
                .mapToObj(c -> (char) c)
                .map(Object::toString)
                .collect(Collectors.joining());
    }

    public static String fromIntStreamToString(IntStream input) {
        return input
                .mapToObj(c -> (char) c)
                .map(Object::toString)
                .collect(Collectors.joining());
    }
}
