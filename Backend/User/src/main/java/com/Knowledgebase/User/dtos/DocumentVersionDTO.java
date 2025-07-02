package com.Knowledgebase.User.dtos;

import com.Knowledgebase.User.entities.DocumentVersion;

import java.time.Instant;

public record DocumentVersionDTO(
        Long id,
        int versionNumber,
        String editorName,
        Instant createdAt
) {
    public static DocumentVersionDTO of(DocumentVersion v) {
        return new DocumentVersionDTO(
                v.getId(),
                v.getVersionNumber(),
                v.getEditor().getName(),
                v.getCreatedAt()
        );
    }
}
