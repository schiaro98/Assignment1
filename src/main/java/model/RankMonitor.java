package main.java.model;

import java.util.HashMap;

public interface RankMonitor {

    boolean update(HashMap<String, Integer> pageRank);

    HashMap<String, Integer> viewMostFrequentN(int n);

    void reset();

    void stop();

    //TODO delete stamp()
    void stamp();
}
