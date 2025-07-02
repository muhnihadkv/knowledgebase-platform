package com.Knowledgebase.User.services;

import com.Knowledgebase.User.dtos.CreateDocRequest;
import com.Knowledgebase.User.dtos.DocumentDTO;
import com.Knowledgebase.User.dtos.DocumentVersionDTO;
import com.Knowledgebase.User.dtos.UpdateDocRequest;
import com.Knowledgebase.User.entities.*;
import com.Knowledgebase.User.repositories.DocumentRepository;
import com.Knowledgebase.User.repositories.DocumentUserAccessRepository;
import com.Knowledgebase.User.repositories.DocumentVersionRepository;
import com.Knowledgebase.User.repositories.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@Transactional
public class DocumentService {
    @Autowired
    private DocumentRepository docRepo;
    @Autowired
    private DocumentVersionRepository versionRepo;
    @Autowired
    private DocumentUserAccessRepository accessRepo;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;

    public int getUserIdFromToken(String token) {
        return jwtService.extractUserId(token.substring(7));
    }
    /* ---------- Create ---------- */
    public DocumentDTO create(CreateDocRequest req, String authHeader) {
        int userId=getUserIdFromToken(authHeader);
        User currentUser = userRepository.findById(userId).orElse(null);
        Document doc = new Document();
        doc.setTitle(req.getTitle());
        doc.setContent(req.getContent());
        doc.setVisibility(req.isPublic() ? Visibility.PUBLIC : Visibility.PRIVATE);
        doc.setAuthor(currentUser);
        docRepo.save(doc);

        // first version
        saveVersion(doc, currentUser);

        return DocumentDTO.of(doc);
    }

    /* ---------- Update (Auto‑save) ---------- */
    public DocumentDTO update(Long id, UpdateDocRequest req, String authHeader) {
        int userId=getUserIdFromToken(authHeader);
        User currentUser = userRepository.findById(userId).orElse(null);
        Document doc = getForEdit(id, currentUser);

        doc.setTitle(req.getTitle());
        doc.setContent(req.getContent());
        doc.setUpdatedAt(Instant.now());

        // every save → new version
        saveVersion(doc, currentUser);

        return DocumentDTO.of(doc);
    }

    /* ---------- Global Search ---------- */
    @Transactional(readOnly = true)
    public List<DocumentDTO> search(String q, String authHeader) {
        List<Document> results = docRepo.fullTextSearch(q);

        int userId = getUserIdFromToken(authHeader);
        User currentUser = userRepository.findById(userId).orElse(null);

        return results.stream()
                .filter(d -> canView(d, currentUser))
                .map(DocumentDTO::of)
                .toList();
    }

    /* ---------- Permission helpers ---------- */
    private boolean canView(Document d, User u) {
        return d.getVisibility() == Visibility.PUBLIC ||
                d.getAuthor().equals(u) ||
                accessRepo.findByDocumentIdAndUserUserId(d.getId(), u.getUserId()).isPresent();
    }

    private Document getForEdit(Long id, User u) {
        Document d = docRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Document not found"));
        boolean allowed = d.getAuthor().equals(u) ||
                accessRepo.findByDocumentIdAndUserUserId(id, u.getUserId())
                        .map(a -> a.getPermission() == Permission.EDIT)
                        .orElse(false);
        if (!allowed) throw new RuntimeException();
        return d;
    }

    private Document getForView(Long id, int userId) {
        Document d = docRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Document not found"));

        boolean allowed = d.getVisibility() == Visibility.PUBLIC ||
                d.getAuthor().getUserId() == userId ||
                accessRepo.findByDocumentIdAndUserUserId(id, userId).isPresent();

        if (!allowed) throw new RuntimeException("You do not have view access");

        return d;
    }


    private void saveVersion(Document doc, User editor) {
        int next = versionRepo.countByDocumentId(doc.getId()) + 1;
        versionRepo.save(new DocumentVersion(null, doc, next,
                doc.getContent(), editor, Instant.now()));
    }

    public List<DocumentDTO> listForUser(String authHeader) {
        // Extract userId from token
        int userId = getUserIdFromToken(authHeader);
        User currentUser = userRepository.findById(userId).orElse(null);

        // Fetch all documents
        List<Document> docs = docRepo.findAll().stream()
                .filter(doc -> canView(doc, currentUser))
                .toList();

        return docs.stream().map(DocumentDTO::of).toList();
    }

    @Transactional(readOnly = true)
    public List<DocumentVersionDTO> getVersionHistory(Long documentId, String authHeader) {

        int userId = getUserIdFromToken(authHeader);
        Document document = getForView(documentId, userId);  // Verify user can access

        List<DocumentVersion> versions = versionRepo.findByDocumentIdOrderByVersionNumberDesc(documentId);

        return versions.stream()
                .map(DocumentVersionDTO::of)
                .toList();
    }

    @Transactional(readOnly = true)
    public String getVersionContent(Long versionId, String authHeader) {

        int userId = getUserIdFromToken(authHeader);

        DocumentVersion version = versionRepo.findById(versionId)
                .orElseThrow(() -> new RuntimeException("Version not found"));

        Document document = getForView(version.getDocument().getId(), userId);

        return version.getContentSnapshot();
    }

    @Transactional(readOnly = true)
    public Map<String, String> compareVersions(Long v1Id, Long v2Id, String authHeader) {

        int userId = getUserIdFromToken(authHeader);

        DocumentVersion v1 = versionRepo.findById(v1Id)
                .orElseThrow(() -> new RuntimeException("Version 1 not found"));

        DocumentVersion v2 = versionRepo.findById(v2Id)
                .orElseThrow(() -> new RuntimeException("Version 2 not found"));

        getForView(v1.getDocument().getId(), userId); // Check permission

        Map<String, String> diffResult = new HashMap<>();
        diffResult.put("version1", v1.getContentSnapshot());
        diffResult.put("version2", v2.getContentSnapshot());

        return diffResult;
    }

    @Transactional
    public void restoreVersion(Long versionId, String authHeader) {

        int userId = getUserIdFromToken(authHeader);
        User currentUser = userRepository.findById(userId).orElse(null);

        DocumentVersion version = versionRepo.findById(versionId)
                .orElseThrow(() -> new RuntimeException("Version not found"));

        Document document = getForEdit(version.getDocument().getId(), currentUser);

        document.setContent(version.getContentSnapshot());
        document.setUpdatedAt(Instant.now());

        saveVersion(document, document.getAuthor()); // Create a new version for the restore
    }




}
