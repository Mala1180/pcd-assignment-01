package nodeadlock_example;

import java.util.ArrayList;
import java.util.List;

public class MyModel {

    private List<ModelObserver> observers;
    private int state;

    private String directoryPath;

    public MyModel(final String directoryPath) {
        state = 0;
        this.directoryPath = directoryPath;
        observers = new ArrayList<ModelObserver>();
    }

    public synchronized void update() {
        state++;
        notifyObservers();
    }

    public synchronized int getState() {
        return state;
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