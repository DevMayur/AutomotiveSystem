package com.mayur.document_vault;

public class DocumentsModel {
    String img_url;
    String title;

    public DocumentsModel(String img_url, String title) {
        this.img_url = img_url;
        this.title = title;
    }

    public DocumentsModel() {
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
