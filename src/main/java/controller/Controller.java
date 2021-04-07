package controller;

import model.*;
import view.View;

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
        this.view = new View(this, monitor);
        monitor.setView(view);
    }

    public void processEvent(String event, String path){
        final String pathFinal = cleanPath(path);
        final int nThread = Runtime.getRuntime().availableProcessors();
//        final int nThread = Runtime.getRuntime().availableProcessors()/2;
//        final int nThread = 1;

        switch(event){
            case "start":
                long start = System.currentTimeMillis();
                view.setStartButtonStatus(false);
                view.reset();
                try {
                    new Thread(() -> {
                        manager.clear();
                        monitor.reset();
                        createTasks(pathFinal, nThread);

                        //read ignore.txt
                        List<String> ignoreWords = getFromIgnoreText(view.getIgnorePath());

                        Set<Worker> workerSet = new HashSet<>();
                        for (int i = 0; i < nThread; i++) {
                            workerSet.add(new Worker(String.valueOf(i), i, manager, monitor, ignoreWords,nThread));
                        }
                        workerSet.forEach(Thread::start);
                        try{
                            for (Worker worker : workerSet) {
                                worker.join();
                            }
                        } catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        view.setStartButtonStatus(true);
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

    private void createTasks(String path, int nThread){
        try {
            Files.walk(Paths.get(path))
                    .filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().endsWith(".pdf"))
                    .collect(Collectors.toSet())
                    .forEach(p ->  manager.add(new Task(String.valueOf(p), nThread)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> getFromIgnoreText(String fileName) {
        File testFile = new File(fileName);
        Scanner inputFile = null;
        try {
            inputFile = new Scanner(testFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<String> words = new ArrayList<>();
        if (inputFile != null) {
            while (inputFile.hasNext()) {
                words.add(inputFile.nextLine());
            }
        } else {
            System.out.println("File Doesn't Exist");
        }
        return words;
    }
}
