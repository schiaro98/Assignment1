package main.java.model;

import java.util.HashMap;
import java.util.List;

public class AnalystWorker extends Thread{

    private final int MAX_TO_WAIT_BEFORE_UPDATING = 500;
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

    //si potrebbe fare uno scheduling a tempo, prendo i currentmillis, se superano un tot aggiorno -> non sono
    //vincolato alla pagine
    @Override
    public void run() {
        long end = 0;
        for (Page p : pages){
            var words =  p.getRelevantWords(unwantedWords);
            long start = System.currentTimeMillis();
            for (String word : words){
                update(word);
                end = System.currentTimeMillis();
                if (end-start > MAX_TO_WAIT_BEFORE_UPDATING){
                    if (!rankMonitor.update(pageRank)){
                        //se torno falso smetto
                        System.out.println("program stopped");
                        break;
                    }
                    start = 0;
                }
            }
            if (!rankMonitor.update(pageRank)){
                //se torno falso smetto
                System.out.println("program stopped");
                break;
            }
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
