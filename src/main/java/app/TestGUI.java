package app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import static java.util.stream.Collectors.toUnmodifiableSet;

public class TestGUI {
    static public void main(String[] args) {


        String path = "./sources";
        Model model = new Model(path);
        Controller controller = new Controller(model);
        View view = new View(controller);
        model.addObserver(view);
        view.setVisible(true);

        Set<Path> files;
        try {
            files = Files.find(Paths.get(model.getDirectoryPath()), Integer.MAX_VALUE, (filePath, fileAttr) -> fileAttr.isRegularFile())
                    .filter(file -> file.toString().endsWith(".java")).collect(toUnmodifiableSet());
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

        for (CounterAgent agent: agents) {
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

}
