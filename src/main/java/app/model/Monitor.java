package app.model;

import app.model.Model;

import java.util.HashMap;
import java.util.Map;

public class Monitor {

    private final Map<String, Integer> distributions = new HashMap<>();
    private final Map<String, Integer> topFiles = new HashMap<>();

    public Monitor(Integer intervals) {
        for (int i = 0; i < intervals; i++) {
            distributions.put("Interval " + i, 0);
        }
        for (int i = 0; i < Model.TOP_FILES_NUMBER; i++) {
            topFiles.put("File " + i, 0);
        }
    }

    public synchronized void updateDistributions(String fileName, Integer fileLines, Integer intervals, Integer maxLines) {
//        Verify.beginAtomic();
        int linesPerInterval = maxLines / (intervals - 1);
        int index = fileLines / linesPerInterval;
        if (fileLines > maxLines) {
            index = intervals - 1;
        }
        String key = "Interval " + (index + 1);
        distributions.put(key, distributions.get(key) + 1);
        if (topFiles.size() < Model.TOP_FILES_NUMBER) {
            topFiles.put(fileName, fileLines);
        } else {
            Map.Entry<String, Integer> minEntry = topFiles.entrySet().stream().min(Map.Entry.comparingByValue()).get();
            if (minEntry.getValue() < fileLines) {
                topFiles.put(fileName, fileLines);
                topFiles.remove(minEntry.getKey());
            }
        }
//        Verify.endAtomic();
    }

    public synchronized Map<String, Integer> getDistributions() {
        return new HashMap<>(this.distributions);
    }

    public synchronized Map<String, Integer> getTopFiles() {
        return new HashMap<>(this.topFiles);
    }

}
