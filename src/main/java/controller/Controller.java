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

    private final View view;
    private final RankMonitor monitor;
    private final Manager manager;
    private final int processors =  Runtime.getRuntime().availableProcessors();

    public Controller(){
        this.manager = new Manager();
        this.monitor = new RankMonitorImpl();
        this.view = new View(this, monitor);
    }

    public void processEvent(String event, String path) throws IOException {
        final String pathFinal = cleanPath(path);
        switch(event){
            case "start":
                try {
                    new Thread(() -> {
                        //VERA IMPLEMENTAZIONE
                        manager.clear();
                        monitor.reset();
                        int nThread = Runtime.getRuntime().availableProcessors();
                        Set<Path> paths = null;
                        try {
                            paths = Files.walk(Paths.get(pathFinal))
                                    .filter(Files::isRegularFile)
                                    .collect(Collectors.toSet());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        paths.forEach(p ->  manager.add(new Task(String.valueOf(p), nThread)));

                        for (int i = 0; i < processors; i++) {
                            new Worker(String.valueOf(i), i, manager, monitor, Arrays.asList("a", "b", "c", "d")).start();
                        }
                        /*HashMap<String, Integer> mostFrequent = monitor.viewMostFrequentN(10);
                        for (String s: mostFrequent.keySet()) {
                            view.addTextToTextArea(view.getTextArea(), "Parola: " + s + " Occorenze: " + mostFrequent.get(s));
                        }*/
                    }).start();
                } catch (Exception ex) {
                    ex.printStackTrace();
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
