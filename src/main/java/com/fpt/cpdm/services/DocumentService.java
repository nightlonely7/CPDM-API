package com.fpt.cpdm.services;

import com.fpt.cpdm.models.documents.Document;
import com.fpt.cpdm.models.documents.DocumentSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DocumentService extends CRUDService<Document> {

    Page<DocumentSummary> findAllSummary(Pageable pageable);

}
