package nodeadlock_example;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

class MyView extends JFrame implements ActionListener, ModelObserver {

	private MyController controller;
	
	public MyView(MyController controller) {
		super("My View");
		
		this.controller = controller;
		
		setSize(500, 400);
		setResizable(false);

		//JFileChooser dirChooser = new JFileChooser();
		//da sostituire con JTextField dirTxt, cosi non devo mettere il path a mano.

		//parameters panel -> BorderLayout.NORTH
		JPanel parametersPanel = new JPanel();

		//dir panel e interval panel inclusi in inline panel (horizzontal)
		JPanel dirPanel = new JPanel();
		JLabel dirLabel = new JLabel("Dir:");
		JTextField dirTxt = new JTextField(20);
		dirPanel.add(dirLabel);
		dirPanel.add(dirTxt);

		JPanel intPanel = new JPanel();
		JLabel intLabel = new JLabel("Int:");
		JTextField intTxt = new JTextField(5);
		intPanel.add(intLabel);
		intPanel.add(intTxt);

		JPanel maxLinesPanel = new JPanel();
		JLabel maxLinesLabel = new JLabel("Max Lines:");
		JTextField maxLinesTxt = new JTextField(5);
		maxLinesPanel.add(maxLinesLabel);
		maxLinesPanel.add(maxLinesTxt);

		JPanel inlinePanel = new JPanel();
		inlinePanel.add(intPanel);
		inlinePanel.add(maxLinesPanel);
		parametersPanel.setLayout(new BoxLayout(parametersPanel, BoxLayout.X_AXIS));

		parametersPanel.add(dirPanel);
		parametersPanel.add(inlinePanel);
		parametersPanel.setLayout(new BoxLayout(parametersPanel, BoxLayout.Y_AXIS));

		//action panel -> BorderLayout.SOUTH
		JPanel actionPanel = new JPanel();

		JButton startBtn = new JButton("Start");
		startBtn.addActionListener(this);
		JButton stopBtn = new JButton("Stop");
		stopBtn.addActionListener(this);

		actionPanel.add(startBtn);
		actionPanel.add(stopBtn);

		//------------------------------------
		
		setLayout(new BorderLayout());
	    add(parametersPanel,BorderLayout.NORTH);
		add(actionPanel,BorderLayout.SOUTH);
	    		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {
				System.exit(-1);
			}
		});
	}
	
	public void actionPerformed(ActionEvent ev) {
		System.out.println(ev.getActionCommand());
		/*if (ev.getSource() == openButton) {
			int returnVal = fc.showOpenDialog(FileChooserDemo.this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				//This is where a real application would open the file.
				log.append("Opening: " + file.getName() + "." + newline);
			} else {
				log.append("Open command cancelled by user." + newline);
			}
		}*/
		try {
			controller.processEvent(ev.getActionCommand());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void modelUpdated(MyModel model) {
		try {
			System.out.println("[View] model updated => updating the view");
			SwingUtilities.invokeLater(() -> {
				//dirTxt.setText("state: " + model.getState());
			});
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}
}
