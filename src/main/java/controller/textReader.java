package main.java.controller;

import java.util.*;
import java.util.stream.Collectors;

public class textReader {
    private final Map<String, Integer> counter = new HashMap<>();

    public textReader(String text, documentReader rd){
        int actualValue=0;
        if(text == null){
            throw new IllegalStateException(this.getClass().getName() + " has received null text to read");
        }
        String[] textSplitted = text.split(rd.getWordSeparator());
        ArrayList<String> words = new ArrayList<>(Arrays.asList(textSplitted));
        for (String word :words) {
            if(!word.equals("")){
                if(counter.containsKey(word)){
                    actualValue=counter.get(word);
                    counter.put(word,actualValue+1);
                } else {
                    counter.put(word,1);
                }
            }
        }
    }

    public int getTextWords(){
        return counter.size();
    }

    public Map<String, Integer> getMostFrequent(int numOfWords){
        Map<String, Integer> sortedMap =
                counter.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                (e1, e2) -> e1, LinkedHashMap::new));
        return sortedMap.entrySet().stream()
                .limit(numOfWords)
                .collect(HashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), Map::putAll);
    }

}