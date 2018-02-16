package com.epam;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public class SimpleGenomeSearcher {
    @NotNull
    public static HashMap<String, HashSet<Integer>> findAllSubseq(@NotNull String input, int size) {
        List<Integer> integerInput = input
                .codePoints()
                .boxed()
                .collect(Collectors.toList());
        LinkedList<Integer> window = integerInput
                .stream()
                .limit(size)
                .collect(Collectors.toCollection(LinkedList::new));
        HashMap<String, HashSet<Integer>> result = new HashMap<>();
        AtomicInteger index = new AtomicInteger();
        result.put(Utils.fromListToString(window), new HashSet<>(Collections.singletonList(index.get())));
        integerInput.stream().skip(size).forEach(i -> {
            window.removeFirst();
            window.add(i);
            index.getAndIncrement();
            String s = Utils.fromListToString(window);
            if (result.containsKey(s)) {
                result.get(s).add(index.get());
            } else {
                result.put(s, new HashSet<>(Collections.singletonList(index.get())));
            }
        });
        return result;
    }
}
