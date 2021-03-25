package main.java.model;

import java.util.List;

public interface PagesMonitor {

    void addPage(Page page);

    void addPages(List<Page> pages);

    List<Page> getSublist(int from, int to);

    int size();

    Page getPage(int index);
}
