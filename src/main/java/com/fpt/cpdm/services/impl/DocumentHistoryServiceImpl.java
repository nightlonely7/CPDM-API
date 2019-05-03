package com.fpt.cpdm.services.impl;

import com.fpt.cpdm.models.documents.document_histories.DocumentHistorySummary;
import com.fpt.cpdm.repositories.DocumentHistoryRepository;
import com.fpt.cpdm.services.DocumentHistoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentHistoryServiceImpl implements DocumentHistoryService {

    private final DocumentHistoryRepository documentHistoryRepository;

    public DocumentHistoryServiceImpl(DocumentHistoryRepository documentHistoryRepository) {
        this.documentHistoryRepository = documentHistoryRepository;
    }

    @Override
    public List<DocumentHistorySummary> findAllSummaryByDocument_Id(Integer documentId) {

        List<DocumentHistorySummary> documentHistorySummaries = documentHistoryRepository
                .findAllSummaryByDocument_Id(documentId);

        return documentHistorySummaries;
    }
}
