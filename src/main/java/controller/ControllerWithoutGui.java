package controller;

import model.*;

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

        this.manager = new Manager();
        RankMonitor monitor = new RankMonitorImpl();

        createTasks(pathFinal, nThread);

        var ignoreWords = getFromIgnoreText(this.ignorePath);

        Set<Worker> workerSet = new HashSet<>();
        for (int i = 0; i < processors; i++) {
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

        var rankToDisplay = monitor.viewMostFrequentN(this.wordsToDisplay);
        displayRank(rankToDisplay);
        System.out.println("Time elapsed " + (System.currentTimeMillis() - start));
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
    private void displayRank(Map<String, Integer> rankToDisplay){
        int totalWords = rankToDisplay.get("TOTAL_WORDS");
        rankToDisplay.remove("TOTAL_WORDS");
        for (Map.Entry<String, Integer> s: rankToDisplay.entrySet()) {
            System.out.println("Parola: " + s.getKey() + " Occorenze: " + s.getValue());
        }
        System.out.println("Numero di parole totali: " + totalWords);
    }
}
