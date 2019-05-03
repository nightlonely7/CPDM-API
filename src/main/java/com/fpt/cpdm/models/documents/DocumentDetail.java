package com.fpt.cpdm.models.documents;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fpt.cpdm.models.NameIdOnly;

import java.time.LocalDateTime;

public interface DocumentDetail {

    Integer getId();

    String getTitle();

    String getSummary();

    String getDescription();

    LocalDateTime getCreatedTime();

    LocalDateTime getStartTime();

    LocalDateTime getEndTime();

    NameIdOnly getProject();
}
