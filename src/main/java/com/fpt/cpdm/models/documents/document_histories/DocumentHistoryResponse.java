package com.fpt.cpdm.models.documents.document_histories;

import com.fpt.cpdm.models.documents.DocumentSummary;
import com.fpt.cpdm.models.users.UserDisplayName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DocumentHistoryResponse {

    private Integer id;
    private DocumentSummary document;
    private UserDisplayName creator;
    private LocalDateTime createdTime;
    private DocumentHistoryDataResponse data;

}
