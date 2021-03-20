package main.java.controller;

import java.util.HashMap;

public interface RankMonitor {

    void update(HashMap<String, Integer> pageRank);

    HashMap<String, Integer> viewMostFrequentN(int n);
}
