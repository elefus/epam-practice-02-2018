package com.epam;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

import static com.epam.Utils.fromIntStreamToString;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SimpleGenomeSearcherTest {
    private List<String> shortStationaryStrings;
    private List<String> largeStationaryStrings;
    private List<String> shortRandomStrings;
    private List<String> largeRandomStrings;
    private Random gen;

    @BeforeEach
    void setUp() {
        gen = new Random();
        shortStationaryStrings = new LinkedList<>();
        IntStream.range(48, 148).parallel().mapToObj(c -> (char) c).forEach(c ->
                shortStationaryStrings.add(fromIntStreamToString(IntStream.generate(() -> c).limit(200))));
        largeStationaryStrings = new LinkedList<>();
        IntStream.range(48, 58).parallel().mapToObj(c -> (char) c).forEach(c ->
                largeStationaryStrings.add(fromIntStreamToString(IntStream.generate(() -> c).limit(2000))));
        shortRandomStrings = new LinkedList<>();
        IntStream.range(0, 100).parallel().forEach(i ->
                shortRandomStrings.add(fromIntStreamToString(IntStream.generate(() ->
                        gen.nextInt() % 99 + 132).limit(200))));
        largeRandomStrings = new LinkedList<>();
        IntStream.range(0, 10).parallel().forEach(i ->
                largeRandomStrings.add(fromIntStreamToString(IntStream.generate(() ->
                        gen.nextInt() % 99 + 132).limit(2000))));
    }

    private void test(List<String> inputs, BiFunction<String, Integer, HashMap<String, HashSet<Integer>>> solver) {
        IntStream.range(0, 5).parallel().forEach(n ->
                inputs.forEach(input ->
                        IntStream.range(15, 20).forEach(size ->
                                solver.apply(input, size).forEach((str, set) ->
                                        set.forEach(i ->
                                                assertEquals(str, input.substring(i, i + str.length())))))));
    }

    @Test
    void shortStationaryPackSimple() {
        test(shortStationaryStrings, SimpleGenomeSearcher::findAllSubseq);
    }

    @Test
    void largeStationaryPackSimple() {
        test(largeStationaryStrings, SimpleGenomeSearcher::findAllSubseq);
    }

    @Test
    void shortRandomPackSimple() {
        test(shortRandomStrings, SimpleGenomeSearcher::findAllSubseq);
    }

    @Test
    void largeRandomPackSimple() {
        test(largeRandomStrings, SimpleGenomeSearcher::findAllSubseq);
    }

    @Test
    void shortStationaryPack() {
        test(shortStationaryStrings, GenomeSearcher::findAllSubseq);
    }

    @Test
    void largeStationaryPack() {
        test(largeStationaryStrings, GenomeSearcher::findAllSubseq);
    }

    @Test
    void shortRandomPack() {
        test(shortRandomStrings, GenomeSearcher::findAllSubseq);
    }

    @Test
    void largeRandomPack() {
        test(largeRandomStrings, GenomeSearcher::findAllSubseq);
    }
}