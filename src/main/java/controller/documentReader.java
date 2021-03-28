package main.java.controller;

import main.java.model.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;


import java.io.*;

import java.util.LinkedList;
import java.util.List;

public class documentReader extends PDFTextStripper{
    //private final String text;
    private final PDDocument document;
    private final Boolean documentPermission;
    private PDFTextStripper stripper;
    private List<Page> pages;


    public documentReader(String path) throws IOException {
        this.document = PDDocument.load(new File(path));
        this.documentPermission = document.getCurrentAccessPermission().canExtractContent();

        this.stripper = new PDFTextStripper();
        pages = new LinkedList<>();
        System.out.println("Started measuring time");
        long start = System.currentTimeMillis();
            /*for (int i = 0; i < document.getNumberOfPages(); i++) {
                this.stripper.setStartPage(i);
                this.stripper.setEndPage(i+1);
                pages.add(new Page(stripper.getText(document).trim()));
            }*/
        this.stripper.setStartPage(0);
        this.stripper.setEndPage(document.getNumberOfPages());
        pages.add(new Page(stripper.getText(document).trim()));
        long end = System.currentTimeMillis();
        System.out.println("To read the entire file we needed: " + (end-start) + " milliseconds");

        //1 doc in più thread
        //Un monitor per ogni pagina/stringa estratta
        //this.text = stripper.getText(document).trim(); //TODO

    }

    public documentReader(String path, PagesMonitor pagesMonitor) throws IOException {
        this.document = PDDocument.load(new File(path));
        this.documentPermission = document.getCurrentAccessPermission().canExtractContent();

        var qzAndResto = new Utility().divideEqually(document.getNumberOfPages());
        int qz = qzAndResto.get(0);
        int resto = qzAndResto.get(1);
        int acc = 0;
        new ReaderWorker("0", acc,(acc+qz+resto), pagesMonitor, path).start();
        acc += qz+resto;
        //System.out.println("first reader has from 0 to "+ acc);
        for (int i = 1; i <  Runtime.getRuntime().availableProcessors(); i++) {
            //System.out.println("Thread "+String.valueOf(i)+ " has pages from "+acc+" to "+(acc+qz) );
            new ReaderWorker(String.valueOf(i), acc,(acc+qz), pagesMonitor, path).start();
            acc = acc+qz;
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    public void stripPage(int pageNr) throws IOException {
        this.setStartPage(pageNr+1);
        this.setEndPage(pageNr+1);
    }

    //public String getText(){return this.text;}

    public PDDocument getDocument() {
        return document;
    }

    public Boolean getDocumentPermission(){
        return documentPermission;
    }

    public int getNumberOfPages(){
        return document.getNumberOfPages();
    }

    public void closeDocument() throws IOException {
        document.close();
    }

    public List<Page> getPages(){ return this.pages; }

    public PDFTextStripper getStripper() {
        return stripper;
    }

    public String getLineSeparator(){
        return this.stripper.getLineSeparator();
    }

    public String getWordSeparator(){
        return this.stripper.getWordSeparator();
    }

}
