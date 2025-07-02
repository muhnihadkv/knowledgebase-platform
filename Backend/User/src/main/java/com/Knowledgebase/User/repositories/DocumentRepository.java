package com.Knowledgebase.User.repositories;

import com.Knowledgebase.User.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DocumentRepository extends JpaRepository<Document,Long>, JpaSpecificationExecutor<Document> {

    // Full‑text search using native query (MySQL 8)
    @Query(
            value = "SELECT * FROM documents WHERE " +
                    "MATCH(title, content) AGAINST (?1 IN NATURAL LANGUAGE MODE)",
            nativeQuery = true)
    List<Document> fullTextSearch(String q);
}
