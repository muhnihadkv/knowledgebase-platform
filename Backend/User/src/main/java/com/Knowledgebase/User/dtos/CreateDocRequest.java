package com.Knowledgebase.User.dtos;

public class CreateDocRequest {
    private String title;
    private String content;
    private boolean publicDocument; // renamed

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isPublicDocument() {
        return publicDocument;
    }

    public void setPublicDocument(boolean publicDocument) {
        this.publicDocument = publicDocument;
    }
}
