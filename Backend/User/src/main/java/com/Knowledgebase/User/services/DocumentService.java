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
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
                accessRepo.findByDocumentIdAndUserId(d.getId(), u.getUserId()).isPresent();
    }

    private Document getForEdit(Long id, User u) {
        Document d = docRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Document not found"));
        boolean allowed = d.getAuthor().equals(u) ||
                accessRepo.findByDocumentIdAndUserId(id, u.getUserId())
                        .map(a -> a.getPermission() == Permission.EDIT)
                        .orElse(false);
        if (!allowed) throw new RuntimeException();
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

}
