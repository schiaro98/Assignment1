package main.java.model;

import java.util.ArrayList;
import java.util.List;

public class Model {

    private List<ModelObserver> observers;

    public Model(){
        observers = new ArrayList<>();
    }

    public void update(){
        //TODO qua devo aggiornare il rank
        notifyObservers();
    }

    public void addObserver(ModelObserver obs){
        observers.add(obs);
    }

    private void notifyObservers(){
        for (ModelObserver obs: observers){
            obs.updateModel(this);
        }
    }
}
