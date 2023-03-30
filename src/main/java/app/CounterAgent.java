package app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

public class CounterAgent extends Thread {

    private final Model model;
    private final Set<Path> filesPerThread;

    public CounterAgent(Model model, Set<Path> filesPerThread) {
        this.model = model;
        this.filesPerThread = filesPerThread;
    }

    public void run() {
        try {
            // count lines
            System.out.println("new thread");

            for (Path file : this.filesPerThread) {
                int lines = 0;
                System.out.println(file.getFileName());
                lines += Files.lines(file).count();
                System.out.println("File " + file.getFileName() + " has lines: " + lines);
                //model.update();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
