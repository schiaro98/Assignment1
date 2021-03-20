package as1;

import main.java.controller.documentCleaner;
import main.java.controller.documentReader;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class testDocumentRead {

    public static final int DEFAULT_REPETITIONS = 10;
    int documentReaden = 0;

    @Disabled
    public void testDocumentReadability() throws IOException {
        //Initialization

        String loremDocumentPath = "res/pdf/item.pdf";
        documentReader rd = new documentReader(loremDocumentPath);

        assertTrue(rd.getDocumentPermission());
        System.out.println("Number of pages: " + rd.getNumberOfPages());
        String text = rd.getText();
        //OR -> String text2 = rd.getStripper().getText();
    }

    @Test
    public void testMultipleDocument() throws IOException {
        /*
            Keep it simple and fast, it is used on timer based test
         */
        String directoryPath = "res/pdf/";
        Set<Path> paths = Files.walk(Paths.get(directoryPath)).filter(Files::isRegularFile).collect(Collectors.toSet());
        for (Path path : paths) {
            documentReader rd = new documentReader(String.valueOf(path));
            assertTrue(rd.getDocumentPermission());
            //String text = rd.getText();
            rd.closeDocument();
            documentReaden++;
        }
    }

    @Test
    public void testTimeSpentOn() throws IOException {
        this.documentReaden=0;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < DEFAULT_REPETITIONS; i++) {
            testMultipleDocument();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("That took " + (endTime - startTime) + "milliseconds to read");
        System.out.println("Num of document readen: " + documentReaden);
    }

    @Test
    public void testIgnoreList() throws IOException {
        String text = "AAAAAAAAAA";
        documentCleaner cl = new documentCleaner(text, "test/ignore/ignoreTest.txt");
        assertEquals("",cl.getCleanedText());

        text="ABC";
        cl.setNewDirtText(text);
        cl.cleanDocument();
        assertEquals("BC",cl.getCleanedText());
    }
}
