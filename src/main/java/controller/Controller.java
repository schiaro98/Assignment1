package main.java.controller;

import main.java.model.*;
import main.java.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Controller {

    private final View view;
    private final RankMonitor monitor;
    private final Manager manager;

    public Controller(){
        this.manager = new Manager();
        this.monitor = new RankMonitorImpl();
        this.view = new View(this, monitor, manager);
        monitor.setView(view);
    }

    public void processEvent(String event, String path){
        final String pathFinal = cleanPath(path);
        final int nThread = Runtime.getRuntime().availableProcessors();
        //final int nThread = 1;
        switch(event){
            case "start":
                long start = System.currentTimeMillis();
                view.setStartButtonStatus(false);
                view.reset();
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
                        /*
                            Qui leggo il file ignore.txt
                         */
                        List<String> ignoreWords = new ArrayList<>();
                        try {
                            ignoreWords = getFromIgnoreText(view.getIgnorePath());
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                       // for (int i = 0; i < processors; i++) {
                      //      new Worker(String.valueOf(i), i, manager, monitor, ignoreWords).start();
                        //}

                        Set<Worker> workerSet = new HashSet<>();
                        for (int i = 0; i < nThread; i++) {
                            workerSet.add(new Worker(String.valueOf(i), i, manager, monitor, ignoreWords));
                        }
                        workerSet.forEach(Thread::start);
                        try{
                            for (Worker worker : workerSet) {
                                worker.join();
                            }
                        } catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        System.out.println("Time elapsed "+ (System.currentTimeMillis() - start));
                    }).start();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;

            case "stop":
                manager.stop();
                view.setStartButtonStatus(true);
                break;
            default:
                throw new IllegalStateException("Error on action!");
        }
    }

    private String cleanPath(String path) {
        if(path.endsWith("/")){
            path = path.substring(0,path.length()-1);
        }
        return path;
    }

    public static List<String> getFromIgnoreText(String fileName) throws FileNotFoundException {
        File testFile = new File(fileName);
        Scanner inputFile = new Scanner(testFile);
        List<String> words = new ArrayList<>();
        if (!testFile.exists()){
            System.out.println("File Doesn't Exist");
            return words;
        }
        while (inputFile.hasNext()) {
            words.add(inputFile.nextLine());
        }
        return words;
    }
}
