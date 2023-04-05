package app;

import utils.Commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import static java.util.stream.Collectors.toSet;

public class Controller {
    private final Model model;

    public Controller(Model model) {
        this.model = model;
    }

    public void setParameters(String directoryPath, Integer intervals, Integer maxLines) {
        model.setParameters(directoryPath, intervals, maxLines);
    }

    public void processEvent(String event) {
        try {
            new Thread(() -> {
                try {
                    switch (Commands.valueOf(event)) {
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
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }).start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    private void startCounting() {
        Set<Path> files;
        try {
            files = Files.find(Paths.get(model.getDirectoryPath()), Integer.MAX_VALUE, (filePath, fileAttr) -> fileAttr.isRegularFile())
                    .filter(file -> file.toString().endsWith(".java") && !file.toString().contains("/file-")).collect(toSet());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int cores = Runtime.getRuntime().availableProcessors() + 1;
        int filesPerCore;
        if (files.size() < cores) {
            filesPerCore = 1;
        } else {
            filesPerCore = files.size() / cores;
        }

        Set<Path> filesPerThread = new HashSet<>();
        Set<CounterAgent> agents = new HashSet<>();
        long startTime = System.currentTimeMillis();

        int counter = 0;
        for (Path file : files) {
            counter++;
            filesPerThread.add(file);
            if (counter % filesPerCore == 0) {
                CounterAgent agent = new CounterAgent(model, new HashSet<>(filesPerThread));
                agent.start();
                agents.add(agent);
                filesPerThread.clear();
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
        System.out.println("Total files: " + files.size());
        System.out.println("Files per core: " + filesPerCore);
        System.out.println("Execution time: " + (stopTime - startTime) + " ms");
    }

    private void stopCounting() {
        //TODO: stop all agents
    }

    private void resetCounter() {
        //TODO: stop all agents, and clear model variables
    }
}