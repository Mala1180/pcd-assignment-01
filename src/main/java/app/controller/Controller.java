package app.controller;

import app.model.CounterAgent;
import app.model.Model;
import app.utils.Event;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Controller {

    private final Model model;

    public Controller(Model model) {
        this.model = model;
    }

    public void setParameters(Integer intervals, Integer maxLines) {
        model.setParameters(intervals, maxLines);
    }

    public void processEvent(Event event) {
        try {
            new Thread(() -> {
                try {
                    switch (event) {
                        case START:
                            startCounting();
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
        String numbers = "123456789";

        for (int i = 0; i < numberOfFiles; i++) {
            strings.add(chars);
            strings.add(numbers);
        }

        int cores = Runtime.getRuntime().availableProcessors() + 1;
        int stringsPerCore = strings.size() < cores ? 1 : strings.size() / cores;

        Set<String> stringsPerThread = new HashSet<>();
        Set<CounterAgent> agents = new HashSet<>();
        long startTime = System.currentTimeMillis();

        int counter = 0;
        for (String string : strings) {
            counter++;
            stringsPerThread.add(string);
            if (counter % stringsPerCore == 0 || (counter == strings.size() && (counter % stringsPerCore) != 0)) {
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

}