package main.java.controller;

import main.java.model.*;

import javax.swing.text.Document;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Controller {

    private Model model;
    private List<Page> pages;
    private RankMonitor monitor = new RankMonitorImpl();

    public Controller(RankMonitorImpl model){
        this.model = model;
    }

    public void processEvent(String event) throws IOException {
        switch(event){
            case "confirm":
                pages = new documentReader("res/pdf/ps.pdf").getPages();
                int processors = Runtime.getRuntime().availableProcessors();
                System.out.println("Processors: " + processors+ " Pages: " + pages.size());
                int qz = getPagesForThread(processors).get(0);
                int resto = getPagesForThread(processors).get(1);
                int acc = 0;
                new AnalystWorker("0", pages.subList(acc,acc+qz+resto), monitor,
                    Arrays.asList("AAAAA")).start();
                acc = acc+qz+resto;
                System.out.println("Acc:" + acc  );
                for (int i = 1; i < processors; i++) {
                    new AnalystWorker(String.valueOf(i), pages.subList(acc,acc+qz), monitor,
                                Arrays.asList("AAAAA")).start();
                    acc = acc+qz;
                    System.out.println("Acc:" + acc  );
                }
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(monitor.viewMostFrequentN(10));
                //Faccio partire thread di analisi
                break;
            case "start":
                System.out.println("start action required");
                break;
            case "pause":
                System.out.println("pause action required");
                break;
            default:
                throw new IllegalStateException("Error on action!");
        }
    }

    private List<Integer> getPagesForThread(int numOfThreads){
        int numOfPages = pages.size();
        List<Integer> pagesForThread = new ArrayList<>();
        pagesForThread.add(numOfPages / numOfThreads);
        pagesForThread.add(numOfPages % numOfThreads);
        return pagesForThread;
    }
}
