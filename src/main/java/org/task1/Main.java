package org.task1;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) {
        int countThreads = 1000;
        Thread maxFrequencyThread = new Thread(() -> {
            List<Map.Entry<Integer, Integer>> sortedList;
            while (!Thread.interrupted()) {
                synchronized (sizeToFreq) {
                    try {
                        sizeToFreq.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    sortedList = sortMap(sizeToFreq);
                }
                System.out.println("Текущий лидер среди частот: " + sortedList.get(0).getKey() + "( встретился " +
                        +sortedList.get(0).getValue() + " раз)");

            }
        });
        maxFrequencyThread.start();

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < countThreads; i++) {
            Thread thread = new Thread(() -> {
                int countR = (int) generateRoute("RLRFR", countThreads)
                        .chars()
                        .filter(x -> x == (int) 'R')
                        .count();
                synchronized (sizeToFreq) {
                    if (sizeToFreq.containsKey(countR)) {
                        sizeToFreq.put(countR, sizeToFreq.get(countR) + 1);
                    } else {
                        sizeToFreq.put(countR, 1);
                    }
                    sizeToFreq.notify(); // Уведомление после каждой итерации
                }
            });
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Прерывание потока, отслеживающего максимум
        maxFrequencyThread.interrupt();
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static List<Map.Entry<Integer, Integer>> sortMap(Map<Integer, Integer> map) {
        return map.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toList());
    }
}