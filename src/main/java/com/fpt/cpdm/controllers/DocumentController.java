package com.fpt.cpdm.controllers;

import com.fpt.cpdm.exceptions.ModelNotValidException;
import com.fpt.cpdm.forms.documents.DocumentCreateForm;
import com.fpt.cpdm.models.documents.Document;
import com.fpt.cpdm.models.documents.DocumentSummary;
import com.fpt.cpdm.services.DocumentService;
import com.fpt.cpdm.utils.ModelErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

//    @GetMapping
//    public ResponseEntity<List<Document>> readAll() {
//
//        List<Document> documents = documentService.findAll();
//        if (documents.isEmpty()) {
//            return ResponseEntity.noContent().build(); //phản hồi trạng thái noContent
//        }
//
//        return ResponseEntity.ok(documents);
//    }

    @GetMapping
    public ResponseEntity<Page<DocumentSummary>> readAll(@PageableDefault Pageable pageable) {

        Page<DocumentSummary> documentSummaries = documentService.findAllSummary(pageable);
        if (documentSummaries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(documentSummaries);
    }


    @PostMapping
    public ResponseEntity<DocumentSummary> create(@Valid @RequestBody DocumentCreateForm documentCreateForm,
                                                  BindingResult result) {
        if (result.hasErrors()) {
            String message = ModelErrorMessage.build(result);
            throw new ModelNotValidException(message);
        }

        DocumentSummary documentSummary = documentService.create(documentCreateForm);

        return ResponseEntity.ok(documentSummary);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Document> update(@PathVariable(name = "id") Integer id,
                                           @Valid @RequestBody Document document,
                                           BindingResult result) {

        return save(id, document, result);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable(name = "id") Integer id) {
    }

    private ResponseEntity<Document> save(Integer id, Document document, BindingResult result) {

        if (result.hasErrors()) {
            String message = ModelErrorMessage.build(result);
            throw new ModelNotValidException(message);
        }
        document.setId(id);

        return null;
    }


}
