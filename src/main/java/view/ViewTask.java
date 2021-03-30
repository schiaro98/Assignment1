package main.java.view;

import main.java.model.Manager;
import main.java.model.RankMonitor;

import java.util.HashMap;
import java.util.TimerTask;

public class ViewTask extends TimerTask {

    private HashMap<String, Integer> mostFrequent;
    private final RankMonitor monitor;
    private final View view;
    private Manager manager;

    public ViewTask(View view, RankMonitor monitor, Manager manager){
        this.view = view;
        this.monitor = monitor;
        this.manager = manager;
        mostFrequent = new HashMap<>();
    }

    @Override
    public void run() {
        /*if(manager.isDone()){
            view.setStartButtonStatus(true);
            this.cancel();
        }*/
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
