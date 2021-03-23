package main.java.controller;

import main.java.model.Page;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class documentReader extends PDFTextStripper{
    //private final String text;
    private final PDDocument document;
    private final Boolean documentPermission;
    private final PDFTextStripper stripper;
    private final List<Page> pages;

    public documentReader(String path) throws IOException {
        this.document = PDDocument.load(new File(path));
        this.documentPermission = document.getCurrentAccessPermission().canExtractContent();
        this.stripper = new PDFTextStripper();
        pages = new LinkedList<>();
        System.out.println("INIZIO");
        /*for (int i = 0; i < document.getNumberOfPages(); i++) {
            this.stripper.setStartPage(i);
            this.stripper.setEndPage(i+1);
            pages.add(new Page(stripper.getText(document).trim()));
        }*/
        this.stripper.setStartPage(0);
        this.stripper.setEndPage(document.getNumberOfPages());
        pages.add(new Page(stripper.getText(document).trim()));
        System.out.println("FINITO");
        //1 doc in più thread
        //Un monitor per ogni pagina/stringa estratta
        //this.text = stripper.getText(document).trim(); //TODO
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
