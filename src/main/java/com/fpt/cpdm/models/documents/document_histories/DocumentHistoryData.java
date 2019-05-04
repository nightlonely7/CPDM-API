package com.fpt.cpdm.models.documents.document_histories;

import lombok.Data;

@Data
public class DocumentHistoryData {

    private String title;
    private String summary;
    private String description;
    private String createdTime;
    private String startTime;
    private String endTime;
    private Integer creatorId;
    private Boolean available;

}
