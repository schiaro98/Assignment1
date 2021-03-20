package main.java.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class documentCleaner {

    private String cleanedText;
    private String dirtText;
    private final ArrayList<String> ignoreList = new ArrayList<>();
    private final String DEFAULT_IGNORE_PATH="res/ignored/ignore.txt";

    public documentCleaner(String text) throws IOException {
        this.dirtText = text;
        readIgnoreList();
        cleanDocument();
    }

    public documentCleaner(String text, String path) throws IOException {
        this.dirtText = text;
        readIgnoreList(path);
        cleanDocument();
    }

    public void cleanDocument(){
            for (String s: ignoreList) {
                this.dirtText = this.dirtText.replaceAll(s, "");
            }
            this.dirtText = this.dirtText.replaceAll("\\.","");
            this.cleanedText = this.dirtText;
        System.out.println("Cleaned: " + this.cleanedText);
    }

    public void readIgnoreList(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line = reader.readLine();
        while(line!=null) {
            ignoreList.add(line);
            line = reader.readLine();
        }
    }

    public void readIgnoreList() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(DEFAULT_IGNORE_PATH));
        String line = reader.readLine();
        while(line!=null) {
            ignoreList.add(line);
            line = reader.readLine();
        }
    }

    public String getCleanedText(){
        return this.cleanedText;
    }

    public void setNewDirtText(String dirtText){
        this.dirtText = dirtText;
    }
}
