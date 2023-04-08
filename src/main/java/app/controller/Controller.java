package app.controller;

import app.model.CounterAgent;
import app.model.Model;
import app.utils.Event;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class Controller {


    private final Model model;

    public Controller(Model model) {
        this.model = model;
    }

    public void setParameters(String directoryPath, Integer intervals, Integer maxLines) {
        model.setParameters(directoryPath, intervals, maxLines);
    }

    public void processEvent(Event event) {
        try {
            new Thread(() -> {
                try {
                    switch (event) {
                        case START:
                            startCounting();
                            break;
                        case STOP:
                            stopCounting();
                            break;
                        case RESET:
                            resetCounter();
                            break;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }).start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    public void startCounting() {
        int numberOfFiles = 50;
        List<String> strings = new ArrayList<>();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz";

        for (int i = 0; i < numberOfFiles; i++) {
//            if (i % 2 == 0) {
//                strings.add(chars);
//                strings.add(chars2);
//            } else {
//                strings.add(numbers);
//            }
            strings.add(chars);
        }


//        Supplier<String> stringSupplier = () -> random.ints(leftLimit, rightLimit + 1)
//                .limit(ThreadLocalRandom.current().nextInt(minBound, maxBound))
//                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
//                .toString();
//
//        Stream<String> stringsStream = Stream.generate(stringSupplier).limit(ThreadLocalRandom.current().nextInt(minBound, maxBound));
//        Set<String> strings = new HashSet<>();
//        Collections.addAll(strings, stringsStream.toArray(String[]::new));

        int cores = Runtime.getRuntime().availableProcessors() + 1;
        int stringsPerCore;
        if (strings.size() < cores) {
            stringsPerCore = 1;
        } else {
            stringsPerCore = strings.size() / cores;
        }

        Set<String> stringsPerThread = new HashSet<>();
        Set<CounterAgent> agents = new HashSet<>();
        long startTime = System.currentTimeMillis();

        int counter = 0;
        for (String string : strings) {
            counter++;
            stringsPerThread.add(string);
            if (counter % stringsPerCore == 0) {
                CounterAgent agent = new CounterAgent(model, new HashSet<>(stringsPerThread));
                agent.start();
                agents.add(agent);
                stringsPerThread.clear();
            }
        }

        for (CounterAgent agent : agents) {
            try {
                agent.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        long stopTime = System.currentTimeMillis();
        System.out.println("Total strings: " + strings.size());
        System.out.println("Files per core: " + stringsPerCore);
        System.out.println("Execution time: " + (stopTime - startTime) + " ms");
    }

    private void stopCounting() {
        //TODO: stop all agents
    }

    private void resetCounter() {
        //TODO: stop all agents, and clear model variables
    }
}