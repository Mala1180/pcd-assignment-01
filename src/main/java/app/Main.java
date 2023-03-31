package app;

public class Main {

    static public void main(String[] args) {
        String path = "./sources";
        Model model = new Model(path);
        Controller controller = new Controller(model);
        View view = new View(controller);
        model.addObserver(view);
        view.setVisible(true);
    }

}