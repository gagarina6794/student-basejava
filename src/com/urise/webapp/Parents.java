package com.urise.webapp;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Parents {

    public static void main(String[] args) {
        List<Integer> people = IntStream.range(1, 2000)
                .boxed()
                .collect(Collectors.toList());

        int counter = 0;

        while (people.size() != 1) {
            counter++;
            System.out.println(counter);
            people.add(people.remove(0));
            people.remove(0);
        }

        System.out.println(people.get(0));
    }

}

