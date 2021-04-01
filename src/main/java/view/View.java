package view;

import controller.Controller;
import model.RankMonitor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class View extends JFrame implements ActionListener {

    private final Controller controller;
    private static final int GLOBAL_WIDTH = 350;
    private static final int GLOBAL_HEIGHT = 500;
    private static final String newline = "\n";
    private final RankMonitor monitor;
    private JTextArea textArea;
    private JTextField directoryText;
    private JTextField wordsCounterText;
    private JTextField ignoreText;
    private JTextField wordsToBePrinted;
    private JButton start;

    public View(Controller controller, RankMonitor monitor){
        JFrame frame = new JFrame("WordsCounter");
        prepareFrame(frame);
        frame.setResizable(false);
        this.controller = controller;
        this.monitor = monitor;
    }

    public void prepareFrame(JFrame frame) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(GLOBAL_WIDTH, GLOBAL_HEIGHT);
        frame.getContentPane().setLayout(new BorderLayout());
        addTextArea(frame);
        addInputAndCounter(frame);
        addButtons(frame);
        frame.setVisible(true);
    }

    private void addInputAndCounter(JFrame frame){
        JPanel dirPanel = new JPanel();
        JLabel directoryLabel = new JLabel("Set directory");
        directoryText = new JTextField("res/pdf",20);
        JLabel ignoreLabel = new JLabel("Set ignore file");
        ignoreText = new JTextField("res/ignored/ignore.txt",20);
        JLabel numOfWordsCounted = new JLabel("Number of words: ");
        wordsCounterText = new JTextField("", 20);
        wordsCounterText.setEditable(false);
        JLabel wordsToBePrintedLabel = new JLabel("Words to be displayed");
        wordsToBePrinted = new JTextField("10",10);
        dirPanel.add(directoryLabel);
        dirPanel.add(directoryText);
        dirPanel.add(ignoreLabel);
        dirPanel.add(ignoreText);
        dirPanel.add(wordsToBePrintedLabel);
        dirPanel.add(wordsToBePrinted);
        dirPanel.add(numOfWordsCounted);
        dirPanel.add(wordsCounterText);
        frame.getContentPane().add(BorderLayout.CENTER, dirPanel);
    }

    private void addButtons(JFrame frame) {
        JPanel panel = new JPanel();
        start = new JButton("Start");
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
        this.textArea = new JTextArea(20, 30);
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

    public int getNumOfWordsToBePrinted(){
        return Integer.parseInt(this.wordsToBePrinted.getText());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(()-> controller.processEvent(e.getActionCommand(), getDirectory()));
    }

    public JTextArea getTextArea(){
        return this.textArea;
    }

    public String getIgnorePath(){
        return this.ignoreText.getText();
    }

    public void setStartButtonStatus(boolean status){
        this.start.setEnabled(status);
    }

    public void rankUpdated(){
        try {
            SwingUtilities.invokeLater(() -> {
                var mostFrequent = monitor.viewMostFrequentN(getNumOfWordsToBePrinted());
                this.updateWordsCounter(mostFrequent.get("TOTAL_WORDS"));
                mostFrequent.remove("TOTAL_WORDS");
                this.getTextArea().setText("");
                for (String s: mostFrequent.keySet()) {
                    this.addTextToTextArea(this.getTextArea(), "Parola: " + s + " Occorenze: " + mostFrequent.get(s));
                }
            });
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void reset(){
        this.textArea.setText("");
        this.wordsCounterText.setText("0");
    }
}