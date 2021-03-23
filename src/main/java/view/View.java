package main.java.view;

import main.java.controller.Controller;
import main.java.model.Model;
import main.java.model.ModelObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class View extends JFrame implements ModelObserver, ActionListener {

    private final Controller controller;
    private static final int GLOBAL_WIDTH = 300;
    private static final int GLOBAL_HEIGHT = 300;

    public View(Controller controller){
        this.controller = controller;
        JFrame frame = new JFrame("WordsCounter");
        prepareFrame(frame);
    }

    public void prepareFrame(JFrame frame) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(GLOBAL_WIDTH, GLOBAL_HEIGHT);
        addDirectoryPanel(frame);
        addButtons(frame);
        frame.setVisible(true);
    }

    private void addDirectoryPanel(JFrame frame){
        JPanel dirPanel = new JPanel();
        JLabel directoryLabel = new JLabel("Set directory");
        JTextField directoryText = new JTextField("path/to/files",20);
        JButton confirmButton = new JButton("Confirm...");
        confirmButton.setActionCommand("confirm");
        /*confirmButton.addActionListener(e -> {
            try {
                if(directoryText.getText().equals("path/to/files")){
                    new Classifier("res/pdf/");
                } else {
                    directoryLabel.setText("Set directory");
                    new Classifier(directoryText.getText());
                }
            } catch (Exception ioException) {
                ioException.printStackTrace();
                directoryLabel.setText("Unable to find directory... Retry");
            }
        });*/
        confirmButton.addActionListener(this);
        dirPanel.add(confirmButton);
        dirPanel.add(directoryLabel);
        dirPanel.add(directoryText);
        frame.getContentPane().add(dirPanel);
    }

    private void addButtons(JFrame frame) {
        JPanel panel = new JPanel();
        JButton start = new JButton("Start");
        start.setActionCommand("start");
        panel.add(start);
        start.addActionListener(this);
        JButton pause = new JButton("Pause");
        pause.setActionCommand("pause");
        pause.addActionListener(this);
        panel.add(pause);
        frame.getContentPane().add(BorderLayout.SOUTH, panel); // Adds Button to content pane of frame
    }

    @Override
    public void updateModel(Model model) {
        //TODO io qua devo recuperare il rank aggiornato e metterlo nella gui
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            controller.processEvent(e.getActionCommand());
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}