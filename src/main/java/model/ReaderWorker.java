package main.java.model;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class ReaderWorker extends Thread{

    private final PagesMonitor pagesMonitor;
    private final int from;
    private final int to;
    private PDFTextStripper stripper;
    private final PDDocument document;

    public ReaderWorker(String name, int from, int to, PagesMonitor pagesMonitor, String path) throws IOException {
        super(name);
        this.from = from;
        this.to = to;
        this.document = PDDocument.load(new File(path));
        //this.documentPermission = document.getCurrentAccessPermission().canExtractContent();;
        this.pagesMonitor = pagesMonitor;
    }

    @Override
    public void run(){
        try {
            System.out.println("Thread "+ getName()+ " begun to read ");
            this.stripper = new PDFTextStripper();
            this.stripper.setStartPage(from);
            this.stripper.setEndPage(to);
            pagesMonitor.addPage(new Page(stripper.getText(document).trim()));
            System.out.println("Thread "+ getName()+ " read his part of the file and ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
