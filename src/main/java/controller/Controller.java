package main.java.controller;

import main.java.model.*;
import main.java.view.View;

import java.io.IOException;
import java.nio.file.Files;
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
        this.view = new View(this, monitor, manager);
    }

    public void processEvent(String event, String path){
        final String pathFinal = cleanPath(path);
        final int nThread = Runtime.getRuntime().availableProcessors();
        switch(event){
            case "start":
                try {
                    new Thread(() -> {
                        //VERA IMPLEMENTAZIONE
                        manager.clear();
                        monitor.reset();
                        try {
                            Files.walk(Paths.get(pathFinal))
                                    .filter(Files::isRegularFile)
                                    .filter(p -> p.getFileName().toString().endsWith(".pdf"))
                                    .collect(Collectors.toSet())
                                    .forEach(p ->  manager.add(new Task(String.valueOf(p), nThread)));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        for (int i = 0; i < processors; i++) {
                            new Worker(String.valueOf(i), i, manager, monitor, Arrays.asList("a", "b", "c", "d")).start();
                        }

                    }).start();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;

            case "stop":
                manager.stop();
                view.cancelTimer();
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
