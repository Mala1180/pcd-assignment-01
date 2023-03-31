package app;

import java.util.*;

public class Model {

    private final List<ModelObserver> observers;
    //private int state;

    private String directoryPath;
    private Integer intervals;
    private Integer maxLines;

    private static final int TOP_FILES_NUMBER = 5;
    private final Map<String, Integer> distributions = new HashMap<>();
    private final Map<String, Integer> topFiles = new HashMap<>();


    public Model(final String directoryPath) {
        //this.state = 0;
        this.directoryPath = directoryPath;
        this.observers = new ArrayList<>();
    }

    public void setParameters(String directoryPath, Integer intervals, Integer maxLines) {
        this.directoryPath = directoryPath;
        this.intervals = intervals;
        this.maxLines = maxLines;
        for (int i = 0; i < intervals; i++) {
            distributions.put("Interval " + (i + 1), 0);
        }
        System.out.println(distributions);
    }

    /*public synchronized void update() {
        //state++;
        //notifyObservers();
    }*/

    public synchronized void updateCounter(String fileName, Integer lines) {
        //TODO: to add monitor for correct accessing to the distribution set
        updateDistribution(fileName, lines);
        notifyObservers();
    }

    private synchronized void updateDistribution(String fileName, Integer fileLines) {
        //TODO with monitor, to decide if in external class
        int linesPerInterval = maxLines / (intervals - 1);
        int index = fileLines / linesPerInterval;
        if (fileLines > maxLines) {
            index = intervals - 1;
        }
        String key = "Interval " + (index + 1);
        System.out.println(key);
        distributions.put(key, distributions.get(key) + 1);
        if (topFiles.size() < TOP_FILES_NUMBER) {
            topFiles.put(fileName, fileLines);
        } else {
            var minEntry = topFiles.entrySet().stream().min(Map.Entry.comparingByValue()).get();
            if (minEntry.getValue() < fileLines) {
                topFiles.put(fileName, fileLines);
                topFiles.remove(minEntry.getKey());
            }
        }
    }

    public synchronized Map<String, Integer> getDistributions() {
        return this.distributions;
    }

    public synchronized Map<String, Integer> getTopFiles() {
        return this.topFiles;
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    public void addObserver(ModelObserver obs) {
        observers.add(obs);
    }

    private void notifyObservers() {
        for (ModelObserver obs : observers) {
            obs.modelUpdated(this);
        }
    }
}
