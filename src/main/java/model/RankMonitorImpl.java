package main.java.model;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class RankMonitorImpl extends Model implements RankMonitor {

    private final HashMap<String, Integer> rank;
    private final Lock mutex;
    private boolean stop;
    private int totalWords;
    private final List<ModelObserver> observers;


    public RankMonitorImpl(){
        observers = new ArrayList<>();
        mutex = new ReentrantLock();
        rank = new HashMap<>();
        stop = false;
        totalWords = 0;
    }

    /*
    TODO nel thread faccio un controllo che la pagina deve avere almeno un carattere
        aka pagerank non vuoto
     */
    @Override
    public boolean update(HashMap<String, Integer> pageRank) {
        notifyObservers();
        try {
			mutex.lock();
			if(!stop){
                for (String s: pageRank.keySet()) {
                    int instancesOfThisWord = pageRank.get(s);
                    if(rank.containsKey(s)){
                        rank.put(s,rank.get(s) + instancesOfThisWord);
                    } else {
                        rank.put(s, instancesOfThisWord);
                    }
                    totalWords += instancesOfThisWord;
                    return true;
                }
            }
            return false;
		} finally {
			mutex.unlock();
		}
    }

    @Override
    public HashMap<String, Integer> viewMostFrequentN(int n) {
        try{
            mutex.lock();
            Map<String, Integer> sortedMap = rank
                    .entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                            (e1, e2) -> e1, LinkedHashMap::new));
            HashMap<String, Integer> sorted = sortedMap.entrySet().stream().limit(n)
                    .collect(HashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), Map::putAll);
            sorted.put("TOTAL_WORDS", totalWords);
            return sorted;
        } finally {
            mutex.unlock();
        }
    }

    @Override
    public void stop() {
        try {
            mutex.lock();
            this.stop = true;
        } finally {
            mutex.unlock();
        }

    }

    private void notifyObservers(){
        for (ModelObserver obs: observers){
            obs.updateModel(this);
        }
    }
}
