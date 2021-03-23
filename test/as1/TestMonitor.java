package as1;

import main.java.model.AnalystWorker;
import main.java.model.Page;
import main.java.model.RankMonitor;
import main.java.model.RankMonitorImpl;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TestMonitor {

    @Test
    public void testPageSeparation(){
        Page page = new Page("prova, prova, prova, prova, prova, prova, prova, gatto, prova, prova, prova");
        List<String> words = page.getRelevantWords(Arrays.asList("gatto"));
        assertEquals(10, words.size());
        for (String s : words){
            assertEquals(s, "prova");
        }
    }

    @Test
    public void testMonitorOnListOfPages(){
        RankMonitor monitor = new RankMonitorImpl();
        List<Page> pages = new LinkedList<>();
        for (int i = 0; i < 40; i++) {
            pages.add(new Page("prova, prova, prova, prova, prova, prova, prova, gatto, prova, prova, prova"));
            pages.add(new Page("pisello, pisello, pisello, pisello, pisello"));
        }
        new AnalystWorker("1", pages.subList(0,20),monitor,Arrays.asList("gatto")).start();
        new AnalystWorker("2", pages.subList(20,40),monitor,Arrays.asList("gatto")).start();
        new AnalystWorker("3", pages.subList(40,60),monitor,Arrays.asList("gatto")).start();
        new AnalystWorker("4", pages.subList(60,80),monitor,Arrays.asList("gatto")).start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        var map = monitor.viewMostFrequentN(2);
        assertEquals(Integer.parseInt("400"), map.get("prova"));
        assertEquals(Integer.parseInt("200"), map.get("pisello"));

    }
}