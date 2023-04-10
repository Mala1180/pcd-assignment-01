package app;

import app.controller.Controller;
import app.model.Model;

public class Main {

    static public void main(String[] args) {
        Model model = new Model();
        Controller controller = new Controller(model);
    }

}