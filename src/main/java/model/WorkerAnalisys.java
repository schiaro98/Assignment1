package main.java.model;

import main.java.controller.Page;

import java.util.HashMap;
import java.util.List;

public class WorkerAnalisys extends Thread{

    //TODO fare dei test per vedere quanto migliora la situazione con arraylist rispetto a linkedlist
    private List<Page> pages;
    private HashMap<String, Integer> pageRank;

    public WorkerAnalisys(List<Page> pages, HashMap<String, Integer> pageRank){
        this.pages = pages;
        this.pageRank = new HashMap<>();
    }


}
