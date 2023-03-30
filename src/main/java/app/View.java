package app;

import utils.Commands;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

class View extends JFrame implements ActionListener, ModelObserver {
    private final Controller controller;

    public View(Controller controller) {
        super("Line Counter");

        this.controller = controller;
        setupGUI();
    }

    public void actionPerformed(ActionEvent ev) {
        try {
            switch (Commands.valueOf(ev.getActionCommand())) {
                case START -> controller.setParameters("./sources", 5, 1000);
                //TODO: read parameters from input fields (above int=5 and maxLines=1000 like in assignment example)
                case RESET -> controller.setParameters("", 0, 0);
                //TODO: to clear input fields
            }
            controller.processEvent(ev.getActionCommand());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void modelUpdated(Model model) {
        try {
            System.out.println("[View] model updated => updating the view");
            SwingUtilities.invokeLater(() -> {
                //dirTxt.setText("state: " + model.getState());
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void setupGUI() {
        setSize(500, 400);
        setResizable(false);

        //JFileChooser dirChooser = new JFileChooser();
        //da sostituire con JTextField dirTxt, cosi non devo mettere il path a mano.

        //------------- START PARAMETERS PANEL -------------

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

        //------------- END PARAMETERS PANEL -------------

        //------------- START LISTS PANEL -------------

        JPanel dataPanel = new JPanel();

        DefaultListModel<String> distributionListModel = new DefaultListModel<>();

        distributionListModel.addElement("Item1");
        distributionListModel.addElement("Item2");
        distributionListModel.addElement("Item3");
        distributionListModel.addElement("Item4");


        JList<String> distributionList = new JList<>(distributionListModel);
        distributionList.setFixedCellWidth(200);
        distributionList.setFixedCellHeight(25);


        DefaultListModel<String> topFilesListModel = new DefaultListModel<>();

        topFilesListModel.addElement("Item1");
        topFilesListModel.addElement("Item2");
        topFilesListModel.addElement("Item3");
        topFilesListModel.addElement("Item4");


        JList<String> topFilesList = new JList<>(topFilesListModel);
        topFilesList.setFixedCellWidth(200);
        topFilesList.setFixedCellHeight(25);

        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.X_AXIS));


        /*JScrollPane scrollPaneDistr = new JScrollPane();
        scrollPaneDistr.setViewportView(distributionList);
        distributionList.setLayoutOrientation(JList.VERTICAL);

        JScrollPane scrollPaneTopFiles = new JScrollPane();
        scrollPaneTopFiles.setViewportView(topFilesList);
        topFilesList.setLayoutOrientation(JList.VERTICAL);*/

        //dataPanel.add(scrollPaneTopFiles);
        dataPanel.add(distributionList);
        dataPanel.add(topFilesList);

        //------------- END LISTS PANEL -------------


        //------------- START ACTION PANEL -------------
        //action panel -> BorderLayout.SOUTH
        JPanel actionPanel = new JPanel();

        JButton startBtn = new JButton("Start");
        startBtn.setActionCommand(Commands.START.toString());
        startBtn.addActionListener(this);
        JButton stopBtn = new JButton("Stop");
        stopBtn.setActionCommand(Commands.STOP.toString());
        stopBtn.addActionListener(this);
        JButton resetBtn = new JButton("Reset");
        resetBtn.setActionCommand(Commands.RESET.toString());
        resetBtn.addActionListener(this);

        actionPanel.add(resetBtn);
        actionPanel.add(startBtn);
        actionPanel.add(stopBtn);

        //------------- END ACTION PANEL -------------

        setLayout(new BorderLayout());
        add(parametersPanel, BorderLayout.NORTH);
        add(dataPanel, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                System.exit(-1);
            }
        });
    }
}
