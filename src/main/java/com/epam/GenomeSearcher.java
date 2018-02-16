package com.epam;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.epam.Utils.polynomStringHash;
import static com.epam.Utils.unsafeFastPow;

public class GenomeSearcher {
    final private static int _HASH_PRIME = 31;
    private static int multiplier = 1;

    private static class HashString {
        @NotNull
        private LinkedList<Character> value;
        private int hash;

        public HashString(@NotNull HashString hs) {
            this.value = new LinkedList<>(hs.getValue());
            this.hash = hs.getHash();
        }
        public HashString(@NotNull String value, int hash) {
            this.value = value
                    .codePoints()
                    .mapToObj(c -> (char) c)
                    .collect(Collectors.toCollection(LinkedList::new));
            this.hash = hash;
        }

        public HashString(@NotNull LinkedList<Character> value, int hash) {
            this.value = value;
            this.hash = hash;
        }

        public HashString(@NotNull String value) {
            this(value, Utils.polynomStringHash(value, _HASH_PRIME));
        }

        public int getHash() {
            return hash;
        }

        public void setHash(int hash) {
            this.hash = hash;
        }

        public void setValue(@NotNull LinkedList<Character> value) {
            this.value = value;
        }

        @NotNull
        public LinkedList<Character> getValue() {
            return value;
        }

        @Override
        public int hashCode() {
            return hash;
        }

        @NotNull
        @Override
        public String toString() {
            return value
                    .stream()
                    .map(Object::toString)
                    .collect(Collectors.joining());
        }

        @Override
        public boolean equals(Object obj) {
            return this.hash == ((HashString) obj).getHash();
        }
    }

    private static void updateWindow(@NotNull HashString window, int newSymbolCode) {
        LinkedList<Character> newValue = window.getValue();
        int oldSymbolCode = newValue.removeFirst();
        newValue.add((char) newSymbolCode);
        window.setHash((window.getHash() - oldSymbolCode) / _HASH_PRIME + newSymbolCode * multiplier);
        window.setValue(newValue);
    }

    @NotNull
    private static ConcurrentHashMap<HashString, HashSet<Integer>> _findAllSubseq(@NotNull String input, int size) {
        multiplier = Utils.unsafeFastPow(_HASH_PRIME, size - 1);
        HashString window = new HashString(input.substring(0, size));
        ConcurrentHashMap<HashString, HashSet<Integer>> result = new ConcurrentHashMap<>();
        AtomicInteger index = new AtomicInteger();
        result.put(new HashString(window), new HashSet<>(Collections.singletonList(index.get())));
        input.codePoints().skip(size).forEach(c -> {
            index.getAndIncrement();
            updateWindow(window, c);
            if (result.containsKey(window)) {
                result.get(window).add(index.get());
            } else {
                result.put(new HashString(window), new HashSet<>(Collections.singletonList(index.get())));
            }
        });
        return result;
    }

    @NotNull
    public static HashMap<String, HashSet<Integer>> findAllSubseq(@NotNull String input, int size) {
        return _findAllSubseq(input, size)
                        .entrySet()
                        .stream()
                        .collect(HashMap::new,
                                (HashMap<String, HashSet<Integer>> map,
                                 Map.Entry<HashString, HashSet<Integer>> entry) ->
                                        map.put(entry.getKey().toString(), entry.getValue()),
                                 HashMap::putAll);
    }
}
