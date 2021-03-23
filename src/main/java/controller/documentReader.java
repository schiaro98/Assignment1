package main.java.controller;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class documentReader {
    private final String text;
    private final PDDocument document;
    private final Boolean documentPermission;
    private final PDFTextStripper stripper;

    public documentReader(String path) throws IOException {
        this.document = PDDocument.load(new File(path));
        this.documentPermission = document.getCurrentAccessPermission().canExtractContent();
        this.stripper = new PDFTextStripper();
        this.stripper.setStartPage(0);
        this.stripper.setEndPage(document.getNumberOfPages());
        //1 doc in più thread
        //Un monitor per ogni pagina/stringa estratta
        this.text = stripper.getText(document).trim(); //TODO
    }

    public String getText(){
        return this.text;
    }

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
