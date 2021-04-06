package controller;

import model.*;
import java.util.*;

public class ControllerJPF{

    private final int wordsToDisplay;
    private static final int PROC = 2;

    public ControllerJPF(String numberWords){
        this.wordsToDisplay = Integer.parseInt(numberWords);
    }

    public void start() {
        System.out.println("Starting processing...");

        Manager manager = new Manager();
        RankMonitor monitor = new RankMonitorImpl();

        for (int i = 0; i < PROC; i++) {
            manager.add(new Task("lorem-ipsum.pdf", PROC));
        }

        Set<WorkerJPF> workerSet = new HashSet<>();
        for (int i = 0; i < PROC; i++) {
            workerSet.add(new WorkerJPF(String.valueOf(i), manager, monitor));
        }
        for (WorkerJPF w: workerSet) {
            w.start();
        }

        try{
            for (WorkerJPF worker : workerSet) {
                worker.join();
            }
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.println("QUA");
        /*Map<String, Integer> rankToDisplay = monitor.viewMostFrequentN(this.wordsToDisplay);
        System.out.println("QUI");
        int totalWords = rankToDisplay.get("TOTAL_WORDS");
        rankToDisplay.remove("TOTAL_WORDS");
        for (Map.Entry<String, Integer> s: rankToDisplay.entrySet()) {
            System.out.println("Parola: " + s.getKey() + " Occorenze: " + s.getValue());
        }
        System.out.println("Numero di parole totali: " + totalWords);*/
        System.exit(0);
        System.out.println("QUI");
    }

}