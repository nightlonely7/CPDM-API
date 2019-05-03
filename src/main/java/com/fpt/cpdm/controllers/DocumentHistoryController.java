package com.fpt.cpdm.controllers;

import com.fpt.cpdm.services.DocumentHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/documents/histories")
public class DocumentHistoryController {

    private final DocumentHistoryService documentHistoryService;

    public DocumentHistoryController(DocumentHistoryService documentHistoryService) {
        this.documentHistoryService = documentHistoryService;
    }

}
