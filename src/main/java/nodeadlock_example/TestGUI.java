package nodeadlock_example;

public class TestGUI {
  static public void main(String[] args){
	  
	MyModel model = new MyModel();
	MyController controller = new MyController(model);
    MyView view = new MyView(controller);
    model.addObserver(view);    
    view.setVisible(true);
    
    //new MyAgent(model).start();

  }
  
}
