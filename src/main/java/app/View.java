package app;

import utils.Commands;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

class View extends JFrame implements ActionListener, ModelObserver {
    private final Controller controller;

    private final DefaultListModel<String> distributionListModel = new DefaultListModel<>();
    private final DefaultListModel<String> topFilesListModel = new DefaultListModel<>();

    private final JFileChooser chooser = new JFileChooser();
    private final JTextField directoryTxt = new JTextField(20);
    private final JTextField intervalsTxt = new JTextField(5);
    private final JTextField maxLinesTxt = new JTextField(5);


    public View(Controller controller) {
        super("Line Counter");
        this.controller = controller;
        setupGUI();
    }

    public void actionPerformed(ActionEvent ev) {
        try {
            switch (Commands.valueOf(ev.getActionCommand())) {
                case START:
                    if(!directoryTxt.getText().equals("")
                            && Integer.parseInt(intervalsTxt.getText()) > 0
                            && Integer.parseInt(maxLinesTxt.getText()) > 0) {
                        controller.setParameters(directoryTxt.getText(), Integer.parseInt(intervalsTxt.getText()), Integer.parseInt(maxLinesTxt.getText()));
                    }
                    break;
                case RESET:
                    resetParameters();
                    break;
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
                this.distributionListModel.clear();
                this.topFilesListModel.clear();
                model.getDistributions().forEach((k, v) -> {
                    this.distributionListModel.addElement(k + " " + v);
                });
                model.getTopFiles().forEach((k, v) -> {
                    this.topFilesListModel.addElement(k + " " + v);
                });
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void setupGUI() {
        setSize(800, 600);
        setResizable(false);


        directoryTxt.setText("/Users/mattia/Desktop/Università/Triennale");
        intervalsTxt.setText("5");
        maxLinesTxt.setText("1000");

        //------------- START PARAMETERS PANEL -------------

        JPanel parametersPanel = new JPanel();

        //dir panel e interval panel inclusi in inline panel (horizontal)
        JPanel dirPanel = new JPanel();
        JLabel dirLabel = new JLabel("Dir:");
        dirPanel.add(dirLabel);
        dirPanel.add(directoryTxt);

        JButton openFileChooserBtn = new JButton("Scegli directory");
        openFileChooserBtn.setActionCommand(Commands.OPEN_FILE_DIALOG.toString());
        openFileChooserBtn.addActionListener(e -> {
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                directoryTxt.setText(chooser.getSelectedFile().toString());
            } else {
                directoryTxt.setText("No Selection");
            }

        });
        dirPanel.add(openFileChooserBtn);

        chooser.setDialogTitle("Choose the directory to scan");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        JPanel intPanel = new JPanel();
        JLabel intLabel = new JLabel("Int:");
        intPanel.add(intLabel);
        intPanel.add(intervalsTxt);

        JPanel maxLinesPanel = new JPanel();
        JLabel maxLinesLabel = new JLabel("Max Lines:");
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

        JList<String> distributionList = new JList<>(distributionListModel);
        distributionList.setFixedCellWidth(200);
        distributionList.setFixedCellHeight(25);

        JList<String> topFilesList = new JList<>(topFilesListModel);
        topFilesList.setFixedCellWidth(200);
        topFilesList.setFixedCellHeight(25);
        topFilesList.setAlignmentX(CENTER_ALIGNMENT);
        distributionList.setAlignmentX(CENTER_ALIGNMENT);
        topFilesList.setAlignmentY(CENTER_ALIGNMENT);
        distributionList.setAlignmentY(CENTER_ALIGNMENT);
        //topFilesList.setAlignmentX(SwingUtilities.CENTER);topFilesList.setAlignmentY(SwingUtilities.CENTER);
        //distributionList.setAlignmentX(SwingUtilities.CENTER);distributionList.setAlignmentY(SwingUtilities.CENTER);


        /*dataPanel.setAlignmentX(CENTER_ALIGNMENT);
        dataPanel.setAlignmentY(CENTER_ALIGNMENT);*/
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.X_AXIS));

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

        this.setLocationRelativeTo( null );

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                System.exit(-1);
            }
        });

    }

    private void resetParameters() {
        distributionListModel.clear();
        topFilesListModel.clear();
        controller.setParameters("", 0, 0);
        directoryTxt.setText("");
        intervalsTxt.setText("");
        maxLinesTxt.setText("");
    }
}
