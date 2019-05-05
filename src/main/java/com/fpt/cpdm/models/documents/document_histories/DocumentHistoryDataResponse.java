package com.fpt.cpdm.models.documents.document_histories;

import com.fpt.cpdm.models.users.UserDisplayName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DocumentHistoryDataResponse {

    private String title;
    private String summary;
    private String description;
    private LocalDateTime createdTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private UserDisplayName creator;
    private Boolean available;

}
