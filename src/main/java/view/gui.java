package main.java.view;

import main.java.controller.Classifier;

import javax.swing.*;
import java.awt.*;

class gui{
    private static final int GLOBAL_WIDTH = 300;
    private static final int GLOBAL_HEIGHT = 300;

    public static void main(String[] args){
       JFrame frame = new JFrame("WordsCounter");
       prepareFrame(frame);
    }

    public static void prepareFrame(JFrame frame) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(GLOBAL_WIDTH, GLOBAL_HEIGHT);
        addDirectoryPanel(frame);
        addButtons(frame);
        frame.setVisible(true);
    }

    private static void addDirectoryPanel(JFrame frame){
        JPanel dirPanel = new JPanel();
        JLabel directoryLabel = new JLabel("Set directory");
        JTextField directoryText = new JTextField("path/to/files",20);
        JButton confirmButton = new JButton("Confirm...");
        confirmButton.addActionListener(e -> {
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
        });
        dirPanel.add(confirmButton);
        dirPanel.add(directoryLabel);
        dirPanel.add(directoryText);
        frame.getContentPane().add(dirPanel);
    }

    private static void addButtons(JFrame frame) {
        JPanel panel = new JPanel();
        JButton start = new JButton("Start");
        JButton pause = new JButton("Pause");
        panel.add(start);
        panel.add(pause);
        frame.getContentPane().add(BorderLayout.SOUTH, panel); // Adds Button to content pane of frame
    }
}