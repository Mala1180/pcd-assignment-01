package app.model;

import app.model.Model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

public class CounterAgent extends Thread {

    private final Model model;
    private final Set<Path> filesPerThread;

    private boolean isStopped = false;

    public CounterAgent(Model model, Set<Path> filesPerThread) {
        this.model = model;
        this.filesPerThread = filesPerThread;
    }

    public void run() {
        try {
            System.out.println("new thread");
            for (Path file : this.filesPerThread) {
                if (isStopped) {
                    break;
                }
                int lines = 0;
                lines += Files.lines(file).count();
                System.out.println("File " + file.getFileName() + " has lines: " + lines);
                model.updateCounter(file.getFileName().toString(), lines);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void setStopped(boolean isStopped) {
        this.isStopped = isStopped;
    }
}
