package com.fpt.cpdm.services;

import com.fpt.cpdm.entities.DocumentEntity;
import com.fpt.cpdm.models.documents.document_histories.DocumentHistoryResponse;
import com.fpt.cpdm.models.documents.document_histories.DocumentHistorySummary;

import java.util.List;

public interface DocumentHistoryService {

    void save(DocumentEntity documentEntity);

    List<DocumentHistorySummary> findAllSummaryByDocument_Id(Integer documentId);

    DocumentHistoryResponse findById(Integer id);
}
