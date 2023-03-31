package app;

import java.util.HashMap;
import java.util.Map;

public class Monitor {

    private final Map<String, Integer> distributions = new HashMap<>();
    private final Map<String, Integer> topFiles = new HashMap<>();

    private boolean available;

    public Monitor(Integer intervals, Integer maxLines) {
        for (int i = 0; i < intervals; i++) {
            distributions.put("Interval " + i, 0);
        }
        for (int i = 0; i < maxLines; i++) {
            topFiles.put("File " + i, 0);
        }
    }

    public synchronized void updateCounter(String fileName, Integer interval, Integer lines, Integer maxLines) {
//        int intervals = distributions.size(); // 5
//        int interval = lines / intervals;
//        int index = lines / interval;
//        String key = "Interval " + interval;
//        distributions.put(key, distributions.get(key) + 1);
//        topFiles.entrySet().stream().min(Map.Entry.comparingByValue()).ifPresent(entry -> {
//            if (entry.getValue() < lines) {
//                topFiles.put(fileName, lines);
//                if (topFiles.size() > maxLines
//                topFiles.remove(entry.getKey());
//            }
//        });
    }

    public synchronized Map<String, Integer> getDistributions() {
        while (!available) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return this.distributions;
    }

}
