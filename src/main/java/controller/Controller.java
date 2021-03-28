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
    private int processors =  Runtime.getRuntime().availableProcessors();

    public Controller(){
        this.view = new View(this);
    }

    public void processEvent(String event, String path) throws IOException {
        switch(event){
            case "start":
               /* new documentReader("res/pdf/nomi.pdf",pagesMonitor);
                //Faccio partire thread di analisi
                for (int i = 0; i < pagesMonitor.size(); i++) {
                    new AnalystWorker(String.valueOf(i), Arrays.asList(pagesMonitor.getPage(i)), monitor,
                            Arrays.asList("cioCCOlata")).start();
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                HashMap<String, Integer> mostFrequent = monitor.viewMostFrequentN(10);
                for (String s: mostFrequent.keySet()) {
                    //Add in textarea
                    view.addTextToTextArea(view.getTextArea(), "Parola: " + s + " Occorenze: " + mostFrequent.get(s));
                }
                monitor.stamp();
                pagesMonitor.clear();*/


                //VERA IMPLEMENTAZIONE
                Manager manager = new Manager();
                monitor = new RankMonitorImpl();
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
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                HashMap<String, Integer> mostFrequent = monitor.viewMostFrequentN(10);
                for (String s: mostFrequent.keySet()) {
                    view.addTextToTextArea(view.getTextArea(), "Parola: " + s + " Occorenze: " + mostFrequent.get(s));
                }

                break;
            case "stop":
                /*
                TODO collegare action di stop del thread
                 */
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
