package com.urise.webapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StreamTask {
    public static void main(String[] args) {
        System.out.println(minValue(new int[]{1, 2, 5, 6, 6, 5, 3, 4}));

        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        list.add(1);
        System.out.println(oddOrEven(list).toString());

    }

    static int minValue(int[] values) {
        Integer[] arrayInteger = IntStream.of(values).boxed().toArray(Integer[]::new);
        var collection = Arrays.stream(arrayInteger);
        return Arrays.stream(collection.distinct().sorted().toArray()).mapToInt(i -> (int) i)
                .reduce(0,(left, right) ->left * 10 + right);
    }

    static List<Integer> oddOrEven(List<Integer> integers) {
        int sum = integers.stream().mapToInt(Integer::intValue).sum();
        return integers.stream().filter(o -> o % 2 != sum % 2).collect(Collectors.toList());
    }
}