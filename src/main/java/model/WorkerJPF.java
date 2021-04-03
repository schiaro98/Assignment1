package model;

import java.util.*;

public class WorkerJPF extends Thread{

    private static final int MAX_TO_WAIT_BEFORE_UPDATING = 500;
    private final Manager manager;
    private final RankMonitor rankMonitor;

    public WorkerJPF(String name, Manager manager, RankMonitor rankMonitor){
        super(name);
        this.manager = manager;
        this.rankMonitor = rankMonitor;
    }

    @Override
    public void run() {
        Page currentPage = null;
        for (Task t : manager.getTasks()){
            if(!t.isDone() && t.isAvailable()){
                if (manager.isComputationStopped()){
                    currentPage = new Page(createText());
                }
                if (currentPage!=null && manager.isComputationStopped()){
                    analyze(currentPage);
                }
                t.incThreadWhoAlreadyWorked();
            }
        }
        //System.out.println("Thread "+getName()+" completed his job... exiting");
    }

    private void analyze(Page page){
        HashMap<String, Integer> pageRank = new HashMap<>();
        long end;
        var words =  new ArrayList<>(Collections.singleton(page.getPage()));
        long start = System.currentTimeMillis();
        for (String word : words){
            update(pageRank, word);
            end = System.currentTimeMillis();
            if (end-start > MAX_TO_WAIT_BEFORE_UPDATING){
                rankMonitor.update(pageRank);
                pageRank.clear();
                start = 0;
            }
        }
        rankMonitor.update(pageRank);
    }

    private void update(HashMap<String, Integer> pageRank, String word){
        if(pageRank.containsKey(word)){
            pageRank.put(word, (pageRank.get(word))+1);
        } else {
            pageRank.put(word,1);
        }
    }

    public String createText(){
        StringBuilder a = new StringBuilder();
        for (int i = 0; i < 1; i++) {
            a.append("THIS IS A TEST");
        }
        return a.toString();
    }
}

