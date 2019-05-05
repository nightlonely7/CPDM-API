package com.fpt.cpdm.controllers;

import com.fpt.cpdm.exceptions.ModelNotValidException;
import com.fpt.cpdm.forms.documents.files.DocumentFileUpdateForm;
import com.fpt.cpdm.models.documents.document_files.DocumentFileDetail;
import com.fpt.cpdm.services.DocumentFileService;
import com.fpt.cpdm.utils.ModelErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/documents/files")
public class DocumentFileController {

    private final DocumentFileService documentFileService;

    @Autowired
    public DocumentFileController(DocumentFileService documentFileService) {
        this.documentFileService = documentFileService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentFileDetail> update(@PathVariable("id") Integer id,
                                                 @Valid DocumentFileUpdateForm documentFileUpdateForm,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = ModelErrorMessage.build(bindingResult);
            throw new ModelNotValidException(message);
        }

        DocumentFileDetail documentFileDetail = documentFileService.update(id, documentFileUpdateForm);

        return ResponseEntity.ok(documentFileDetail);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Integer id) {

        documentFileService.delete(id);

        return ResponseEntity.noContent().build();
    }

}
