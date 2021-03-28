package main.java.view;

import main.java.controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class View extends JFrame implements ActionListener {

    private final Controller controller;
    private static final int GLOBAL_WIDTH = 400;
    private static final int GLOBAL_HEIGHT = 400;
    private static final String newline = "\n";
    private JTextArea textArea;
    private JTextField directoryText;

    public View(Controller controller){
        this.controller = controller;
        JFrame frame = new JFrame("WordsCounter");
        prepareFrame(frame);
    }

    public void prepareFrame(JFrame frame) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(GLOBAL_WIDTH, GLOBAL_HEIGHT);
        frame.getContentPane().setLayout(new BorderLayout());
        addTextArea(frame);
        addDirectoryPanel(frame);
        addButtons(frame);
        frame.setVisible(true);
    }

    private void addDirectoryPanel(JFrame frame){
        JPanel dirPanel = new JPanel();
        JLabel directoryLabel = new JLabel("Set directory");
        directoryText = new JTextField("res/pdf",20);
        dirPanel.add(directoryLabel);
        dirPanel.add(directoryText);
        frame.getContentPane().add(BorderLayout.CENTER, dirPanel);
    }

    private void addButtons(JFrame frame) {
        JPanel panel = new JPanel();
        JButton start = new JButton("Start");
        start.setActionCommand("start");
        start.addActionListener(this);
        JButton stop = new JButton("Stop");
        stop.setActionCommand("stop");
        stop.addActionListener(this);
        panel.add(start);
        panel.add(stop);
        frame.getContentPane().add(BorderLayout.SOUTH, panel); // Adds Button to content pane of frame
    }

    private void addTextArea(JFrame frame){
        JPanel textPanel = new JPanel();
        this.textArea = new JTextArea(8, 30);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textPanel.add(new JScrollPane(textArea));
        frame.getContentPane().add(BorderLayout.NORTH, textPanel);
    }

    public void addTextToTextArea(JTextArea textArea, String text){
        textArea.append(text + newline);
    }

    public String getDirectory(){
        return this.directoryText.getText();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            controller.processEvent(e.getActionCommand(), getDirectory());
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public JTextArea getTextArea(){
        return this.textArea;
    }
}