package main.java.model;

import java.util.ArrayList;
import java.util.List;

public class Utility {



    public List<Integer> divideEqually(int divideEqually){
        int numOfThreads =  Runtime.getRuntime().availableProcessors();
        List<Integer> pagesForThread = new ArrayList<>();
        pagesForThread.add(divideEqually / numOfThreads);
        pagesForThread.add(divideEqually % numOfThreads);
        return pagesForThread;
    }
}
