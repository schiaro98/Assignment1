package controller;

import model.*;
import java.util.*;

public class ControllerJPF{

    private final int processors =  2;
    private final int wordsToDisplay;

    public ControllerJPF(String numberWords){
        this.wordsToDisplay = Integer.parseInt(numberWords);
    }

    public void start() {
        System.out.println("Starting processing...");

        Manager manager = new Manager();
        RankMonitor monitor = new RankMonitorImpl();

        for (int i = 0; i < 2; i++) {
            manager.add(new Task("lorem-ipsum.pdf", processors));
        }

        Set<WorkerJPF> workerSet = new HashSet<>();
        for (int i = 0; i < processors; i++) {
            workerSet.add(new WorkerJPF(String.valueOf(i), manager, monitor));
        }
        workerSet.forEach(Thread::start);

        try{
            for (WorkerJPF worker : workerSet) {
                worker.join();
            }
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        var rankToDisplay = monitor.viewMostFrequentN(this.wordsToDisplay);
        int totalWords = rankToDisplay.get("TOTAL_WORDS");
        rankToDisplay.remove("TOTAL_WORDS");
        for (Map.Entry<String, Integer> s: rankToDisplay.entrySet()) {
            System.out.println("Parola: " + s.getKey() + " Occorenze: " + s.getValue());
        }
        System.out.println("Numero di parole totali: " + totalWords);
    }


    public static List<String> getFromIgnoreText()  {
        List<String> words = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            words.add("test");
        }
        return words;
    }
}

