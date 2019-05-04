package com.fpt.cpdm.controllers;

import com.fpt.cpdm.models.documents.document_histories.DocumentHistoryResponse;
import com.fpt.cpdm.services.DocumentHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/documents/histories")
public class DocumentHistoryController {

    private final DocumentHistoryService documentHistoryService;

    public DocumentHistoryController(DocumentHistoryService documentHistoryService) {
        this.documentHistoryService = documentHistoryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentHistoryResponse> readAllHistories(@PathVariable("id") Integer id) {

        DocumentHistoryResponse documentHistoryResponse = documentHistoryService.findById(id);

        return ResponseEntity.ok(documentHistoryResponse);
    }

}
