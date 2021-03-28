package main.java.controller;

import main.java.model.*;
import main.java.view.View;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Controller {

    private View view;
    private RankMonitor monitor;
    private Manager manager;
    private int processors =  Runtime.getRuntime().availableProcessors();

    public Controller(){
        this.view = new View(this);
        this.manager = new Manager();
        this.monitor = new RankMonitorImpl();
    }

    public void processEvent(String event, String path) throws IOException {
        switch(event){
            case "start":
                //VERA IMPLEMENTAZIONE
                manager.clear();
                monitor.reset();
                int nThread = Runtime.getRuntime().availableProcessors();
                path = cleanPath(path);
                Set<Path> paths = Files.walk(Paths.get(path))
                        .filter(Files::isRegularFile)
                        .collect(Collectors.toSet());
                paths.forEach(p ->  manager.add(new Task(String.valueOf(p), nThread)));

                for (int i = 0; i < processors; i++) {
                    new Worker(String.valueOf(i), i, manager, monitor, Arrays.asList("a", "b", "c", "d")).start();
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                HashMap<String, Integer> mostFrequent = monitor.viewMostFrequentN(10);
                for (String s: mostFrequent.keySet()) {
                    view.addTextToTextArea(view.getTextArea(), "Parola: " + s + " Occorenze: " + mostFrequent.get(s));
                }

                break;
            case "stop":
                manager.stop();
                break;
            default:
                throw new IllegalStateException("Error on action!");
        }
    }

    private String cleanPath(String path) {
        if(path.startsWith("/")){
            path = path.substring(1);
        }
        if(path.endsWith("/")){
            path = path.substring(0,path.length()-1);
        }
        return path;
    }
}
