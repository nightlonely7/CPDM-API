package com.fpt.cpdm.services;

import com.fpt.cpdm.forms.documents.DocumentCreateForm;
import com.fpt.cpdm.models.documents.DocumentSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DocumentService {

    Page<DocumentSummary> findAllSummary(Pageable pageable);

    DocumentSummary create(DocumentCreateForm documentCreateForm);
}
