package app.model;


import java.util.*;

public class Model {

    public static final int TOP_FILES_NUMBER = 5;
    private Integer intervals;
    private Integer maxLines;
    private Monitor monitor;


    public Model() {
    }

    public void setParameters(Integer intervals, Integer maxLines) {
        this.intervals = intervals;
        this.maxLines = maxLines;
        this.monitor = new Monitor(intervals);
    }

    public void updateCounter(String fileName, Integer lines) {
        this.monitor.updateDistributions(fileName, lines, intervals, maxLines);
    }

    public Map<String, Integer> getDistributions() {
        return this.monitor.getDistributions();
    }

    public Map<String, Integer> getTopFiles() {
        return this.monitor.getTopFiles();
    }
}
