package com.fpt.cpdm.models.tasks;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public interface TaskDetail extends TaskSummary {

    String getDescription();

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime getLastModifiedTime();
}
