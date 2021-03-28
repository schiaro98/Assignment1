package main.java.controller;

import main.java.model.*;
import main.java.view.View;

import javax.swing.text.Document;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Controller {

    private View view;
    private Model model;
    private List<Page> pages;
    private RankMonitor monitor = new RankMonitorImpl();
    private PagesMonitor pagesMonitor = new PagesMonitorImpl();
    private int processors =  Runtime.getRuntime().availableProcessors();

    public Controller(RankMonitorImpl model){
        this.model = model;
        this.view = new View(this);
    }

    public void processEvent(String event) throws IOException {
        switch(event){
            case "start":
                new documentReader("res/pdf/nomi.pdf",pagesMonitor);
                /* System.out.println("Processors: " + processors+ " Pages: " + pages.size());
                int qz = getPagesForThread(processors).get(0);
                int resto = getPagesForThread(processors).get(1);
                int acc = 0;
                new AnalystWorker("0", pagesMonitor.getSublist(acc,acc+qz+resto), monitor,
                    Arrays.asList("XX")).start();
                acc = acc+qz+resto;
                System.out.println("Acc:" + acc  );
                for (int i = 1; i < processors; i++) {
                    System.out.println("Thread "+String.valueOf(i)+ " has pages from "+acc+" to "+(acc+qz) );
                    new AnalystWorker(String.valueOf(i), pagesMonitor.getSublist(acc,acc+qz), monitor,
                                Arrays.asList("XX")).start();
                    acc = acc+qz;
                    System.out.println("Acc:" + acc  );
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(monitor.viewMostFrequentN(10));
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                monitor.stamp();*/
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
                pagesMonitor.clear();





                //VERA IMPLEMENTAZIONE
                Manager manager = new Manager();
                int nThread = Runtime.getRuntime().availableProcessors();
                String realPath = "res/pdf/";
                Set<Path> paths = Files.walk(Paths.get(realPath)).filter(Files::isRegularFile).collect(Collectors.toSet());
                for (Path p : paths) {
                    manager.add(new Task(String.valueOf(p), nThread));
                }

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
