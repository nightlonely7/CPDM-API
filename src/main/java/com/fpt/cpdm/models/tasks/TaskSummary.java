package com.fpt.cpdm.models.tasks;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fpt.cpdm.models.NameIdOnly;
import com.fpt.cpdm.models.users.UserDisplayName;

import java.time.LocalDateTime;

public interface TaskSummary {

    Integer getId();

    String getTitle();

    String getSummary();

    NameIdOnly getProject();

    UserDisplayName getExecutor();

    UserDisplayName getCreator();

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime getCreatedTime();

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime getStartTime();

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime getEndTime();

    Integer getPriority();

    String getStatus();
}
