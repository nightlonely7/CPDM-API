package com.fpt.cpdm.services;

import com.fpt.cpdm.forms.documents.DocumentCreateForm;
import com.fpt.cpdm.models.documents.DocumentSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DocumentService {

    List<DocumentSummary> findAllSummaryByProjectId(Integer projectId);

    Page<DocumentSummary> findAllSummary(Pageable pageable);

    Page<DocumentSummary> findAllSummaryByRelatives(Pageable pageable);

    DocumentSummary create(DocumentCreateForm documentCreateForm);
}
