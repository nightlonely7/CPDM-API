package com.fpt.cpdm.services;

import com.fpt.cpdm.models.documents.document_histories.DocumentHistorySummary;

import java.util.List;

public interface DocumentHistoryService {

    List<DocumentHistorySummary> findAllSummaryByDocument_Id(Integer documentId);

}
