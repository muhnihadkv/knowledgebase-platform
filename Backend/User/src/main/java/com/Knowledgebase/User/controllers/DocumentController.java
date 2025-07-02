package com.Knowledgebase.User.controllers;

import com.Knowledgebase.User.dtos.*;
import com.Knowledgebase.User.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {
    @Autowired
    private DocumentService docService;
    //private AuthFacade auth;      // helper to fetch User from SecurityContext

    @PostMapping("/create")
    public DocumentDTO create(@RequestHeader("Authorization") String authHeader,@RequestBody CreateDocRequest req) {
        return docService.create(req, authHeader);
    }

    @PutMapping("/update/{id}")
    public DocumentDTO update(@PathVariable Long id,
                              @RequestBody UpdateDocRequest req,
                              @RequestHeader("Authorization") String authHeader) {
        return docService.update(id, req, authHeader);
    }

    @GetMapping("/listForUser")
    public List<DocumentDTO> list(@RequestHeader("Authorization") String authHeader) {
        return docService.listForUser(authHeader);
    }

    @GetMapping("/search")
    public List<DocumentDTO> search(@RequestHeader("Authorization") String authHeader,
                                    @RequestParam String q) {
        return docService.search(q, authHeader);
    }

    @GetMapping("/{id}/versions")
    public List<DocumentVersionDTO> getVersionHistory(@PathVariable Long id,
                                                      @RequestHeader("Authorization") String authHeader) {
        return docService.getVersionHistory(id, authHeader);
    }

    @GetMapping("/versions/{versionId}")
    public String getVersionContent(@PathVariable Long versionId,
                                    @RequestHeader("Authorization") String authHeader) {
        return docService.getVersionContent(versionId, authHeader);
    }

    @GetMapping("/versions/compare")
    public Map<String, String> compareVersions(@RequestParam Long v1,
                                               @RequestParam Long v2,
                                               @RequestHeader("Authorization") String authHeader) {
        return docService.compareVersions(v1, v2, authHeader);
    }

    @PostMapping("/versions/{versionId}/restore")
    public void restoreVersion(@PathVariable Long versionId,
                               @RequestHeader("Authorization") String authHeader) {
        docService.restoreVersion(versionId, authHeader);
    }

    @GetMapping("/public/{id}")
    public DocumentDTO getPublicDocument(@PathVariable Long id) {
        return docService.getPublicDocument(id);
    }

    @PostMapping("/{id}/share")
    public ResponseEntity<String> shareDocument(@PathVariable Long id,
                                                @RequestHeader("Authorization") String authHeader,
                                                @RequestBody ShareRequest request) {
        docService.shareDocument(id, authHeader, request);
        return ResponseEntity.ok("User access added successfully");
    }

    @DeleteMapping("/{id}/share")
    public ResponseEntity<String> removeAccess(@PathVariable Long id,
                                               @RequestHeader("Authorization") String authHeader,
                                               @RequestBody ShareRequest request) {
        docService.removeUserAccess(id, authHeader, request);
        return ResponseEntity.ok("User access removed successfully");
    }

}

