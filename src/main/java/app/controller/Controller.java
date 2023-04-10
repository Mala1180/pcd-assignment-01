package app.controller;

import app.model.CounterAgent;
import app.model.Model;
import app.utils.Event;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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


    Set<CounterAgent> agents;

    public void startCounting() {
        Set<Path> files;
        if (model.getDirectoryPath() == null) {
            return;
        }
        try (Stream<Path> stream = Files.find(Paths.get(model.getDirectoryPath()), Integer.MAX_VALUE, (filePath, fileAttr) -> fileAttr.isRegularFile())) {
            files = stream.filter(file -> file.toString().endsWith(".java")).collect(toSet());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int cores = Runtime.getRuntime().availableProcessors() + 1;
        int averageFilesPerThread = files.size() < cores ? 1 : files.size() / cores;

        List<Path> filesPerThread = new ArrayList<>();
        int lastThreadFiles = 0;
        agents = new HashSet<>();
        long startTime = System.currentTimeMillis();

        int counter = 0;

        for (Path file : files) {
            filesPerThread.add(file);

            if ((counter % averageFilesPerThread == 0 && counter < averageFilesPerThread * (cores - 1)) || counter == files.size() - 1) {
                CounterAgent agent = new CounterAgent(model, new HashSet<>(filesPerThread));
                agent.start();
                agents.add(agent);
                lastThreadFiles = filesPerThread.size();
                System.out.println(lastThreadFiles);
                filesPerThread.clear();
            }
            counter++;
        }

        for (CounterAgent agent : agents) {
            try {
                agent.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        long stopTime = System.currentTimeMillis();
        System.out.println("Total files: " + files.size());
        System.out.println("Files per core: " + averageFilesPerThread);
        System.out.println("Files last core: " + lastThreadFiles);

        System.out.println("Execution time: " + (stopTime - startTime) + " ms");
    }

    private void stopCounting() {
        for (CounterAgent agent : agents) {
            agent.setStopped(true);
        }
    }

    public void resetCounter() {
        setParameters("", 0, 0);
    }
}