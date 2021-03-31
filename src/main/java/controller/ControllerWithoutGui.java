package main.java.controller;

import main.java.model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class ControllerWithoutGui {

    private final int processors =  Runtime.getRuntime().availableProcessors();
    private final String directoryPath;
    private final String ignorePath;
    private final int wordsToDisplay;
    private RankMonitor monitor;
    private Manager manager;

    public ControllerWithoutGui(String path, String ignore, String numberWords){
        this.directoryPath = path;
        this.ignorePath = ignore;
        this.wordsToDisplay = Integer.parseInt(numberWords);
    }

    public void start(){
        System.out.println("Starting processing...");
        long start = System.currentTimeMillis();
        final String pathFinal = cleanPath(directoryPath);
        final int nThread = Runtime.getRuntime().availableProcessors();
        //VERA IMPLEMENTAZIONE
        this.manager = new Manager();
        this.monitor = new RankMonitorImpl();
        try {
            Files.walk(Paths.get(pathFinal))
                    .filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().endsWith(".pdf"))
                    .collect(Collectors.toSet())
                    .forEach(p ->  manager.add(new Task(String.valueOf(p), nThread)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> ignoreWords = new ArrayList<>();
        try {
            ignoreWords = getFromIgnoreText(this.ignorePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Set<Worker> workerSet = new HashSet<>();
        for (int i = 0; i < processors; i++) {
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
        var rankToDisplay = monitor.viewMostFrequentN(this.wordsToDisplay);
        int totalWords = rankToDisplay.get("TOTAL_WORDS");
        rankToDisplay.remove("TOTAL_WORDS");
        for (String s: rankToDisplay.keySet()) {
            System.out.println("Parola: " + s + " Occorenze: " + rankToDisplay.get(s));
        }
        System.out.println("Numero di parole totali: " + totalWords);
        System.out.println("Time elapsed " + (System.currentTimeMillis() - start));
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
