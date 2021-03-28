package main.java.model;

import java.util.HashMap;

public class Worker extends Thread{

    private Task currentTask;
    private Manager manager;
    private RankMonitor rankMonitor;
    private final HashMap<String, Integer> pageRank;


    public Worker(Manager manager, RankMonitor rankMonitor){
        this.manager = manager;
        this.rankMonitor = rankMonitor;
        this.pageRank = new HashMap<>();
    }

    @Override
    public void run() {

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
