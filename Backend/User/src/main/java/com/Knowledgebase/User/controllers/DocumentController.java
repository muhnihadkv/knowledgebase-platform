package com.Knowledgebase.User.controllers;

import com.Knowledgebase.User.dtos.CreateDocRequest;
import com.Knowledgebase.User.dtos.DocumentDTO;
import com.Knowledgebase.User.dtos.UpdateDocRequest;
import com.Knowledgebase.User.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public List<DocumentDTO> list(@RequestHeader("Authorization") String authHeader) {
        return docService.listForUser(authHeader);
    }

    @GetMapping("/search")
    public List<DocumentDTO> search(@RequestHeader("Authorization") String authHeader,
                                    @RequestParam String q) {
        return docService.search(q, authHeader);
    }
}

