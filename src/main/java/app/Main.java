package app;

public class Main {

    static public void main(String[] args) {
        String path = "/Users/mattia/Desktop";
        Model model = new Model(path);
        Controller controller = new Controller(model);
        View view = new View(controller);
        model.addObserver(view);
        view.setVisible(true);
    }

}