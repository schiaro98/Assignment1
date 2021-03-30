package main.java.model;

import java.util.HashMap;
import java.util.LinkedHashMap;

public interface RankMonitor {

    boolean update(HashMap<String, Integer> pageRank);

    LinkedHashMap<String, Integer> viewMostFrequentN(int n);

    void reset();

    void stop();

    //TODO delete stamp()
    void stamp();
}
