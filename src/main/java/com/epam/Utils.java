package com.epam;


import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Spliterators.spliteratorUnknownSize;

public class Utils {
    public static byte[] clz = new byte[65536];
    static {
        for (int i = 0; i < 65536; ++i) {
            clz[i] = (byte) Long.numberOfLeadingZeros(i);
        }
    }
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


    public static String toBin(long l) {
        StringBuilder result = new StringBuilder("");
        for(int i = 0; i < Long.numberOfLeadingZeros(l); i++) {
            result.append('0');
        }
        return result.append(Long.toBinaryString(l)).toString();
    }

    public static String printBinLongArray(long[] data) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                Arrays
                        .stream(data)
                        .boxed()
                        .collect(Collectors.toCollection(LinkedList::new))
                        .descendingIterator(),
                Spliterator.ORDERED),
                false)
                .map(Utils::toBin)
                .collect(Collectors.joining(" "));
    }

    /*
     * works ONLY with sizes which are divider of 64 (e. g. 1, 2, 4, 8, 16, 32, 64)
     * size -- how much do you want pack byte in one long
     */
    public static long[] unsafeCompress(byte[] data, byte size) {
        byte multiplier = (byte) (64 / size);
        long[] result = new long[data.length / size];
        long mask = ~(-(1 << multiplier));
        for (int i = 0; i < data.length / size; ++i) {
            result[i] = 0;
            for (int j = 0; j < size; ++j) {
                result[i] += (data[i * multiplier  + j] & mask) << (j * multiplier);
            }
        }
        return result;
    }
}
