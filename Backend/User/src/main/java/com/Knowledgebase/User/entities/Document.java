package com.Knowledgebase.User.entities;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "documents", indexes = {
        @Index(name = "idx_document_visibility", columnList = "visibility")
})
public class Document {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Lob @Basic(fetch = FetchType.LAZY)
    private String content;                       // HTML / Markdown from the WYSIWYG

    @Enumerated(EnumType.STRING)
    private Visibility visibility = Visibility.PRIVATE;

    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    @Version                                      // optimistic locking
    private Long version;                         // for collision detection

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
