package app;

import java.nio.file.Path;
import java.util.*;

public class Model {

    private final List<ModelObserver> observers;
    //private int state;

    private String directoryPath;
    private Integer interval;
    private Integer maxLines;

    private final Map<String, Integer> distributions = new HashMap<>();
    private final Set<Path> topFiles = new HashSet<>();

    public Model(final String directoryPath) {
        //this.state = 0;
        this.directoryPath = directoryPath;
        this.observers = new ArrayList<>();
    }

    public void setParameters(String directoryPath, Integer intervals, Integer maxLines) {
        this.directoryPath = directoryPath;
        this.interval = intervals;
        this.maxLines = maxLines;
        for (int i = 0; i < intervals; i++) {
            distributions.put("Interval " + i, 0);
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

    private void updateDistribution(String fileName, Integer lines) {
        //TODO with monitor, to decide if in external class
        System.out.println(fileName + " --> " + lines);
    }

    /*public synchronized int getState() {
        return state;
    }*/

    public synchronized Map<String, Integer> getDistributions() {
        return this.distributions;
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
