import model.RankMonitor;
import view.View;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RankMonitorImplJpf implements RankMonitor {
    private final HashMap<String, Integer> rank;
    private final Lock mutex;
    private final boolean stop;
    private int totalWords;
    private View view;

    public RankMonitorImplJpf(){
        mutex = new ReentrantLock();
        rank = new HashMap<>();
        stop = false;
        totalWords = 0;
    }

    public void setView(View view){
        this.view = view;
    }

    public void update(HashMap<String, Integer> pageRank) {
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
            }
            notifyView();
        } finally {
            mutex.unlock();
        }
    }

    public Map<String, Integer> viewMostFrequentN(int n) {
        try{
            mutex.lock();
            Map<String, Integer> sortedMap = new LinkedHashMap<>();
            Map<String, Integer> tmp = sortByValue(rank);

            Iterator<String> it = tmp.keySet().iterator();
            while(it.hasNext() && n>0){
                String key = it.next();
                sortedMap.put(key, rank.get(key));
                n--;
            }
            //Sort, limit(n), Put in sorted map

            sortedMap.put("TOTAL_WORDS", totalWords);
            return sortedMap;
        } finally {
            mutex.unlock();
        }
    }

    public Map<String, Integer> getMap() {
        try{
            mutex.lock();
            Map<String, Integer> tmp = new LinkedHashMap<>(rank);
            tmp.put("TOTAL_WORDS", totalWords);
            return tmp;
        } finally {
            mutex.unlock();
        }
    }

    public static Map<String, Integer> sortByValue(HashMap<String, Integer> map) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());

        Map<String, Integer> result = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    @Override
    public void reset() {
        try {
            mutex.lock();
            rank.clear();
            this.totalWords = 0;
        }finally {
            mutex.unlock();
        }
    }

    private void notifyView(){
        if (this.view!=null){
            view.rankUpdated();
        }
    }
}
