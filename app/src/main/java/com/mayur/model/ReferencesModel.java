package com.mayur.model;

public class ReferencesModel {
    String line1,line2,line3,pdf_link;

    public ReferencesModel() {
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getLine3() {
        return line3;
    }

    public void setLine3(String line3) {
        this.line3 = line3;
    }

    public String getPdf_link() {
        return pdf_link;
    }

    public void setPdf_link(String pdf_link) {
        this.pdf_link = pdf_link;
    }

    public ReferencesModel(String line1, String line2, String line3, String pdf_link) {
        this.line1 = line1;
        this.line2 = line2;
        this.line3 = line3;
        this.pdf_link = pdf_link;
    }
}
