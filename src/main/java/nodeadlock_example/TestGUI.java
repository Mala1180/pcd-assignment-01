package nodeadlock_example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class TestGUI {
    static public void main(String[] args) {


        String path = "./sources";
        MyModel model = new MyModel(path);
        MyController controller = new MyController(model);
        MyView view = new MyView(controller);
        model.addObserver(view);
        view.setVisible(true);

        Set<Path> files;
        try {
            files = Files.find(Paths.get(model.getDirectoryPath()), Integer.MAX_VALUE, (filePath, fileAttr) -> fileAttr.isRegularFile())
                    .filter(file -> file.toString().endsWith(".java")).collect(Collectors.toUnmodifiableSet());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int cores =  3;// Runtime.getRuntime().availableProcessors() + 1;
        int filesPerCore;
        if (files.size() < cores) {
            filesPerCore = 1;
        } else {
            filesPerCore = files.size() / cores;
        }

        Set<Path> filesPerThread = new HashSet<>();
        Set<MyAgent> agents = new HashSet<>();
        long startTime = System.currentTimeMillis();

        int counter = 0;
        for (Path file : files) {
            counter++;
            filesPerThread.add(file);
            if (counter % filesPerCore == 0) {
                MyAgent agent = new MyAgent(model, new HashSet<>(filesPerThread));
                agent.start();
                agents.add(agent);
                filesPerThread.clear();
            }
        }

        for (MyAgent agent: agents) {
            try {
                agent.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        long stopTime = System.currentTimeMillis();
        System.out.println("Total files: " + files.size());
        System.out.println("Files per cqore: " + filesPerCore);
        System.out.println("Execution time: " + (stopTime - startTime) + " ms");
    }

}
