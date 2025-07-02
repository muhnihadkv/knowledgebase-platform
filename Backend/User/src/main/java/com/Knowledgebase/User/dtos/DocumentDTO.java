package com.Knowledgebase.User.dtos;


import com.Knowledgebase.User.entities.Document;
import com.Knowledgebase.User.entities.Visibility;

import java.time.Instant;

public record DocumentDTO(
        Long id,
        String title,
        Visibility visibility,
        String author,
        Instant updatedAt
) {

    public static DocumentDTO of(Document d) {
        return new DocumentDTO(
                d.getId(),
                d.getTitle(),
                d.getVisibility(),
                d.getAuthor().getName(),
                d.getUpdatedAt()
        );
    }
}

