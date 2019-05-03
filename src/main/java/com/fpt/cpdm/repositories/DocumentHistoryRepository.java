package com.fpt.cpdm.repositories;

import com.fpt.cpdm.entities.DocumentHistoryEntity;
import com.fpt.cpdm.models.documents.document_histories.DocumentHistorySummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentHistoryRepository extends JpaRepository<DocumentHistoryEntity, Integer> {

    List<DocumentHistorySummary> findAllSummaryByDocument_Id(Integer documentId);

}
