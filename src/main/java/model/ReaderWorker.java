package main.java.model;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class ReaderWorker extends Thread{

    private final PagesMonitor pagesMonitor;
    private final int from;
    private final int to;
    private final PDDocument document;

    public ReaderWorker(String name, int from, int to, PagesMonitor pagesMonitor, String path) throws IOException {
        super(name);
        this.from = from+1;
        this.to = to;
        this.document = PDDocument.load(new File(path));
        //this.documentPermission = document.getCurrentAccessPermission().canExtractContent();;
        this.pagesMonitor = pagesMonitor;
        System.out.println("thread "+name+" from "+from+" to "+ to);
    }

    @Override
    public void run(){
        try {
            //System.out.println("Thread "+ getName()+ " begun to read ");

            long start = System.currentTimeMillis();
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setStartPage(from);
            //System.out.println("Thread "+ getName() +" from:"+from);
            stripper.setEndPage(to);
            Page p = new Page(stripper.getText(document).trim());
            System.out.println("Thread "+ getName() +" read:");
            System.out.println(p.getPage());
            pagesMonitor.addPage(p);
            //System.out.println("Thread "+ getName()+ " read his part of the file");
            long end = System.currentTimeMillis();
            System.out.println("Thread "+ getName()+ " read file in "+ (end-start)+" millis");
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
