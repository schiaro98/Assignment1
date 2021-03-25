package main.java.model;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PagesMonitorImpl implements PagesMonitor{

    private final Lock mutex;
    private final List<Page> pages;

    public PagesMonitorImpl() {
        this.mutex = new ReentrantLock();
        this.pages = new LinkedList<>();
    }

    @Override
    public void addPage(Page page) {
        try {
            mutex.lock();
            this.pages.add(page);
        } finally {
            mutex.unlock();
        }
    }

    @Override
    public void addPages(List<Page> pages) {
        try {
            mutex.lock();
            this.pages.addAll(pages);
        } finally {
            mutex.unlock();
        }
    }

    @Override
    public List<Page> getSublist(int from, int to) {
        try {
            mutex.lock();
            return pages.subList(from,to);
        } finally {
            mutex.unlock();
        }
    }

    @Override
    public int size() {
        try {
            mutex.lock();
            return pages.size();
        } finally {
            mutex.unlock();
        }
    }

    @Override
    public Page getPage(int index) {
        try {
            mutex.lock();
            return pages.get(index);
        } finally {
            mutex.unlock();
        }
    }
}
