package main.java.model;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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
     * @param unwantedWords words to delete from string
     * @return string without unwantedWords
     */
    public List<String> getRelevantWords(List<String> unwantedWords){
        this.page = page.toLowerCase();
        for(String s : unwantedWords){
            page = page.replace(" "+s.toLowerCase()+" ", " ");
        }
        return Arrays.asList(page.split("\\W+"));
    }
}
