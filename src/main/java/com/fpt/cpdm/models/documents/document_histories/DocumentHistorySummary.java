package com.fpt.cpdm.models.documents.document_histories;

import com.fpt.cpdm.models.documents.DocumentSummary;
import com.fpt.cpdm.models.users.UserDisplayName;

import java.time.LocalDateTime;

public interface DocumentHistorySummary {
    Integer getId();
    DocumentSummary getDocument();
    UserDisplayName getCreator();
    LocalDateTime getCreatedTime();
}
