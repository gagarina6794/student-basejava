package com.urise.webapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StreamTask {
    public static void main(String[] args) {

        System.out.println(minValue(new int[]{9,8}));
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
        var resultArray = Arrays.stream(collection.distinct().sorted().toArray()).mapToInt(i -> (int) i).toArray();
        int sum = 0;
        for (int value : resultArray) {
            sum *= 10;
            sum += value;
        }
        return sum;
    }

    static List<Integer> oddOrEven(List<Integer> integers) {
        int sum = integers.stream().mapToInt(Integer::intValue).sum();
        if (sum % 2 == 0) {
            return integers.stream().filter(o -> o % 2 == 0).collect(Collectors.toList());
        } else {
            return integers.stream().filter(o -> o % 2 != 0).collect(Collectors.toList());
        }
    }
}