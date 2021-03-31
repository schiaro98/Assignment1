package model;

import view.View;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public interface RankMonitor {

    boolean update(HashMap<String, Integer> pageRank);

    Map<String, Integer> viewMostFrequentN(int n);

    void reset();

    void stop();

    void setView(View view);
}
