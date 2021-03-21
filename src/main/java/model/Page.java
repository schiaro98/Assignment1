package main.java.model;

import java.util.Arrays;
import java.util.List;

public class Page {


    private String page;

    public Page(String page){
        this.page = page;

    }

    public String getPage() {
        return page;
    }

    /**
     * remove unwanted words from page and return all the other words as a List of String
     * @param unwantedWords
     * @return
     */
    public List<String> getRelevantWords(List<String> unwantedWords){
        for(String s : unwantedWords){
            page = page.replace(s, "");
        }
        return Arrays.asList(page.split("\\W+"));
    }
}
