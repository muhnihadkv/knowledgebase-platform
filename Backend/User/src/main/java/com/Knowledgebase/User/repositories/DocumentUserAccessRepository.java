package com.Knowledgebase.User.repositories;

import com.Knowledgebase.User.entities.DocumentUserAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface DocumentUserAccessRepository extends JpaRepository<DocumentUserAccess, Long> {
    Optional<DocumentUserAccess> findByDocumentIdAndUserUserId(Long docId, int userId);
    List<DocumentUserAccess> findAllByUserUserId(int userId);
}
