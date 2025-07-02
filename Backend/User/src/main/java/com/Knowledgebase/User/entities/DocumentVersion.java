package com.Knowledgebase.User.entities;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "document_versions")
public class DocumentVersion {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)            // parent doc
    private Document document;

    private int versionNumber;                    // 1,2,3…

    @Lob @Basic(fetch = FetchType.LAZY)
    private String contentSnapshot;

    @ManyToOne(fetch = FetchType.LAZY)
    private User editor;

    private Instant createdAt = Instant.now();

    public DocumentVersion(Long id, Document document, int versionNumber, String contentSnapshot, User editor, Instant createdAt) {
        this.id = id;
        this.document = document;
        this.versionNumber = versionNumber;
        this.contentSnapshot = contentSnapshot;
        this.editor = editor;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getContentSnapshot() {
        return contentSnapshot;
    }

    public void setContentSnapshot(String contentSnapshot) {
        this.contentSnapshot = contentSnapshot;
    }

    public User getEditor() {
        return editor;
    }

    public void setEditor(User editor) {
        this.editor = editor;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}