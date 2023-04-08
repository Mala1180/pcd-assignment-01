package app.model;

import java.util.Set;

public class CounterAgent extends Thread {

    private final Model model;
    private final Set<String> stringsPerThread;

    public CounterAgent(Model model, Set<String> stringsPerThread) {
        this.model = model;
        this.stringsPerThread = stringsPerThread;
    }

    public void run() {
        System.out.println("new thread");
        for (String string : this.stringsPerThread) {
            int chars = string.length();
//            System.out.println("Chars: " + chars);
            //model.update();
            model.updateCounter(string, chars);
        }

    }
}
