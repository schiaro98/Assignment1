package main.java.view;

import main.java.model.RankMonitor;

import java.util.HashMap;
import java.util.TimerTask;

public class Task extends TimerTask {

    private HashMap<String, Integer> mostFrequent;
    private final RankMonitor monitor;
    private final View view;

    public Task(View view, RankMonitor monitor){
        this.view = view;
        this.monitor = monitor;
        mostFrequent = new HashMap<>();
    }

    @Override
    public void run() {
        mostFrequent = monitor.viewMostFrequentN(10);
        int totalOfWords = mostFrequent.get("TOTAL_WORDS");
        mostFrequent.remove("TOTAL_WORDS");
        view.getTextArea().setText("");
        for (String s: mostFrequent.keySet()) {
            view.addTextToTextArea(view.getTextArea(), "Parola: " + s + " Occorenze: " + mostFrequent.get(s));
        }
        view.updateWordsCounter(totalOfWords);
    }
}
