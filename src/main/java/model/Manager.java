package main.java.model;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Manager{

    private List<Task> tasks = new LinkedList<>();
    private final Lock mutex;

    public Manager() {
        mutex = new ReentrantLock();
    }

    public void add(Task e){
        try{
            mutex.lock();
            tasks.add(e);
        } finally {
            mutex.unlock();
        }
    }

    public boolean isDone(){
        for (Task t : tasks){
         if (!t.isDone()){
            return false;
         }
        }
        return true;
    }
}
