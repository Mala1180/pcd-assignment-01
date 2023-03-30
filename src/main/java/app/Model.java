package app;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Model {

    private final List<ModelObserver> observers;
    //private int state;

    private String directoryPath;
    private Integer interval;
    private Integer maxLines;

    private Set<Integer> distribution;

    public Model(final String directoryPath) {
        //this.state = 0;
        this.directoryPath = directoryPath;
        this.observers = new ArrayList<>();
        this.distribution = new HashSet<>();
    }

    public void setParameters(String directoryPath, Integer interval, Integer maxLines) {
        this.directoryPath = directoryPath;
        this.interval = interval;
        this.maxLines = maxLines;
        //TODO: inizializing the set with interval dimension, slide es: 5
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

    public synchronized Set<Integer> getDistribution() {
        return this.distribution;
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
