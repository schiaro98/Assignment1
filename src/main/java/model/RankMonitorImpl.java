package main.java.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class RankMonitorImpl implements RankMonitor {

    private final HashMap<String, Integer> rank;
    private final Lock mutex;
    private boolean stop;

    public RankMonitorImpl(){
        mutex = new ReentrantLock();
        rank = new HashMap<>();
        stop = false;
    }

    /*
    TODO nel thread faccio un controllo che la pagina deve avere almeno un carattere
        aka pagerank non vuoto
     */
    @Override
    public synchronized void update(HashMap<String, Integer> pageRank) {
        try {
			mutex.lock();
			if(!stop){
                for (String s: pageRank.keySet()) {
                    if(rank.containsKey(s)){
                        rank.put(s,rank.get(s)+ pageRank.get(s));
                    } else {
                        rank.put(s, pageRank.get(s));
                    }
                }
            }
		} finally {
			mutex.unlock();
		}
    }

    @Override
    public synchronized HashMap<String, Integer> viewMostFrequentN(int n) {
        try{
            mutex.lock();
            Map<String, Integer> sortedMap = rank
                    .entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                            (e1, e2) -> e1, LinkedHashMap::new));
            return sortedMap.entrySet().stream()
                    .limit(n)
                    .collect(HashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), Map::putAll);
        } finally {
            mutex.unlock();
        }
    }

    @Override
    public void stop() {
        this.stop = true;
    }
}
