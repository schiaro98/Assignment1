import model.Manager;
import model.RankMonitor;
import model.Task;

import java.util.HashMap;

public class WorkerJPF extends Thread{

    private final Manager manager;
    private final RankMonitor rankMonitor;

    public WorkerJPF(String name, Manager manager, RankMonitor rankMonitor){
        super("Worker" + name);
        this.manager = manager;
        this.rankMonitor = rankMonitor;
    }

    @Override
    public void run() {
        String currentPage = null;
        for (Task t : manager.getTasks()){
            if(!t.isDone() && t.isAvailable()){
                if (manager.isComputationStopped()){
                    currentPage = "TEST";
                }
                if (currentPage!=null && manager.isComputationStopped()){
                    analyze(currentPage);
                }
                t.incThreadWhoAlreadyWorked();
            }
        }
        //System.out.println("Thread "+getName()+" completed his job... exiting");
    }

    private void analyze(String page){
        HashMap<String, Integer> pageRank = new HashMap<>();
        String[] words = page.split(" ");
        for (String word : words){
            update(pageRank, word);
            rankMonitor.update(pageRank);
            pageRank.clear();
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
}

