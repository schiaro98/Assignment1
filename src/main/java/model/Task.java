package main.java.model;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Task {

    private String path;
    private final int maxNumberOfThread;
    private int threadWhoAlreadyWorked;
    private boolean done;
    private Lock mutex;

    public Task(String path, int numberOfThread) {
        this.path = path;
        this.done = false;
        this.threadWhoAlreadyWorked = 0;
        this.mutex = new ReentrantLock();
        this.maxNumberOfThread = numberOfThread;
    }

    public void incThreadWhoAlreadyWorked(){
        try {
            mutex.lock();
            this.threadWhoAlreadyWorked++;
            if (threadWhoAlreadyWorked == maxNumberOfThread){
                done = true;
            }
        }finally {
            mutex.unlock();
        }

    }

    public boolean isDone(){
        try {
            mutex.lock();
            return this.done;
        }finally {
            mutex.unlock();
        }
    }

}
