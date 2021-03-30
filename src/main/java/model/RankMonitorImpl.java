package main.java.model;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RankMonitorImpl implements RankMonitor {

    private final HashMap<String, Integer> rank;
    private final Lock mutex;
    private boolean stop;
    private int totalWords;

    public RankMonitorImpl(){
        mutex = new ReentrantLock();
        rank = new HashMap<>();
        stop = false;
        totalWords = 0;
    }

    /*
    TODO nel thread faccio un controllo che la pagina deve avere almeno un carattere
        aka pagerank non vuoto
     */
    public boolean update(HashMap<String, Integer> pageRank) {
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
                }
                return true;
            }
            return false;
		} finally {
			mutex.unlock();
		}
    }

    public HashMap<String, Integer> viewMostFrequentN(int n) {
        try{
            mutex.lock();
            HashMap<String, Integer> sortedMap = new HashMap<>();

            rank.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));

            HashMap<String, Integer> sorted = sortedMap.entrySet().stream().limit(n)
                    .collect(HashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), Map::putAll);
            sorted.put("TOTAL_WORDS", totalWords);
            return sorted;
        } finally {
            mutex.unlock();
        }
    }

    @Override
    public void reset() {
        try {
            mutex.lock();
            rank.clear();
        }finally {
            mutex.unlock();
        }
    }

    public void stamp(){
        for (String s :rank.keySet()){
            System.out.println("Word "+s+" appeared:   " +rank.get(s)+ " times." );
        }
    }

    public void stop() {
        try {
            mutex.lock();
            this.stop = true;
            this.totalWords = 0;
        } finally {
            mutex.unlock();
        }
    }
}
