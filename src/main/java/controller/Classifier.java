package main.java.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

public class Classifier {
    int documentReaden=0;
    String defaultPath = "res/pdf/";
    String realPath = "";
    HashMap<String, Integer> frequentWords = new HashMap<>();

    public Classifier(String path) throws IOException {
        if(!path.equals("")){
            realPath=path;
        } else {
            realPath=defaultPath;
        }

        Set<Path> paths = Files.walk(Paths.get(realPath)).filter(Files::isRegularFile).collect(Collectors.toSet());
        for (Path p : paths) {
            documentReaden++;
            System.out.println("---DOCUMENT #"+documentReaden+"---");
            documentReader rd = new documentReader(String.valueOf(p));
            rd.closeDocument();
            documentCleaner cleaner = new documentCleaner(rd.getText());
            textReader words = new textReader(cleaner.getCleanedText(), rd);

            frequentWords.putAll(words.getMostFrequent(10));
            for (String key:frequentWords.keySet()) {
                System.out.println("Word: " + key + " | Occurences: " + frequentWords.get(key));
            }
            frequentWords.clear();

        }
    }
}
