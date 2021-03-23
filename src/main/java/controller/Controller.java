package main.java.controller;

import main.java.model.Model;
import main.java.model.RankMonitorImpl;

public class Controller {

    private Model model;

    public Controller(RankMonitorImpl model){
        this.model = model;
    }

    public void processEvent(String event){
        System.out.println(event);//TODO rimuovere
        switch(event){
            case "confirm":
                System.out.println("Confirm action required");
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
}
