package main.java.model;

import java.util.HashMap;
import java.util.List;

public class AnalystWorker extends Thread{

    //TODO fare dei test per vedere quanto migliora la situazione con arraylist rispetto a linkedlist
    private final List<Page> pages;
    private final HashMap<String, Integer> pageRank;
    private final RankMonitor rankMonitor;
    private final List<String> unwantedWords;

    public AnalystWorker(String name, List<Page> pages, RankMonitor rankMonitor, List<String> unwantedWords){
        super(name);
        this.pages = pages;
        this.pageRank = new HashMap<>();
        this.unwantedWords = unwantedWords;
        this.rankMonitor = rankMonitor;
    }

    @Override
    public void run() {
        for (Page p : pages){
            for (String word : p.getRelevantWords(unwantedWords)){
                update(word);
            }
            rankMonitor.update(pageRank);
            pageRank.clear();
        }
        System.out.println("Thread "+getName()+" completed his job... exiting");
    }

    private void update(String word){
        if(pageRank.containsKey(word)){
            pageRank.put(word, (pageRank.get(word))+1);
        } else {
            pageRank.put(word,1);
        }
    }
}
