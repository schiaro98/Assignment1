package main.java.view;

import main.java.model.RankMonitor;

import java.util.HashMap;
import java.util.TimerTask;

public class Task extends TimerTask {

    HashMap<String, Integer> mostFrequent;
    RankMonitor monitor;
    View view;

    public Task(View view, RankMonitor monitor){
        this.view = view;
        this.monitor = monitor;
    }

    @Override
    public void run() {
        mostFrequent = monitor.viewMostFrequentN(10);
        int totalOfWords = mostFrequent.get("TOTAL_WORDS");
        mostFrequent.remove("TOTAL_WORDS");
        for (String s: mostFrequent.keySet()) {
            view.addTextToTextArea(view.getTextArea(), "Parola: " + s + " Occorenze: " + mostFrequent.get(s));
        }
        view.updateWordsCounter(totalOfWords);
    }
}
