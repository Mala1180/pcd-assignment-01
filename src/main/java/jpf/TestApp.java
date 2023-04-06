package jpf;

import app.controller.Controller;
import app.model.Model;
import app.utils.Event;

public class TestApp {


    /**
     * Main method for testing with java pathfinder
     */
    static public void main(String[] args) {
        Model model = new Model();
        model.setParameters(System.getProperty("user.dir"), 5, 100);
        Controller controller = new Controller(model);
        controller.processEvent(Event.START);
    }
}
