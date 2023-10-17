package org.task1;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) {
        int countThreads = 100;
        String route;
        for (int i = 0; i < countThreads; i++) {
            new Thread(() -> {
                synchronized (sizeToFreq) {
                    //route = generateRoute("RLRFR", countThreads);
                    int countR = (int) generateRoute("RLRFR", countThreads)
                            .chars()
                            .filter(x -> x == (int) 'R')
                            .count();
                    if (sizeToFreq.containsKey(countR)) {
                        sizeToFreq.put(countR, sizeToFreq.get(countR) + 1);
                    } else {
                        sizeToFreq.put(countR, 1);
                    }
                }
            }).start();
        }


        List<Map.Entry<Integer, Integer>> sortedList = sizeToFreq.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toList());

        System.out.println("Самое частое количество повторений " + sortedList.get(0).getKey() + " (встретилось " +
                sortedList.get(0).getValue() + " раз)");

        System.out.println("Другие размеры:");
        for (int i = 1; i < sortedList.size(); i++) {
            System.out.println("- " + sortedList.get(i).getKey() + " (" + sortedList.get(i).getValue() + " раз)");
        }
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}