package com.Knowledgebase.User.repositories;

import com.Knowledgebase.User.entities.DocumentVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentVersionRepository extends JpaRepository<DocumentVersion, Long> {
    int countByDocumentId(Long id);
    List<DocumentVersion> findByDocumentIdOrderByVersionNumberDesc(Long documentId);
}