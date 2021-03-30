package main.java.view;

import main.java.controller.Controller;
import main.java.model.RankMonitor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Timer;

public class View extends JFrame implements ActionListener {

    private final Controller controller;
    private static final int GLOBAL_WIDTH = 400;
    private static final int GLOBAL_HEIGHT = 400;
    private static final String newline = "\n";
    private JTextArea textArea;
    private JTextField directoryText;
    private JTextField wordsCounterText;
    private final RankMonitor monitor;

    public View(Controller controller, RankMonitor monitor){
        JFrame frame = new JFrame("WordsCounter");
        prepareFrame(frame);
        this.controller = controller;
        this.monitor = monitor;
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new Task(this, monitor), 100, 500);
    }

    public void prepareFrame(JFrame frame) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(GLOBAL_WIDTH, GLOBAL_HEIGHT);
        frame.getContentPane().setLayout(new BorderLayout());
        addTextArea(frame);
        addDirectory(frame);
        addButtons(frame);
        frame.setVisible(true);
    }

    private void addDirectory(JFrame frame){
        JPanel dirPanel = new JPanel();
        JLabel directoryLabel = new JLabel("Set directory");
        directoryText = new JTextField("res/pdf",20);
        JLabel numOfWordsCounted = new JLabel("Number of words: ");
        wordsCounterText = new JTextField("", 20);
        wordsCounterText.setEditable(false);
        dirPanel.add(directoryLabel);
        dirPanel.add(directoryText);
        dirPanel.add(numOfWordsCounted);
        dirPanel.add(wordsCounterText);
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

    public void updateWordsCounter(int value){
        this.wordsCounterText.setText(String.valueOf(value));
    }

    public String getDirectory(){
        return this.directoryText.getText();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(()->{
            try {
                controller.processEvent(e.getActionCommand(), getDirectory());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }

    public JTextArea getTextArea(){
        return this.textArea;
    }
}