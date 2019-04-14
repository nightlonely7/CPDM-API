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

    TaskBasic getParentTask();

    UserDisplayName getExecutor();

    UserDisplayName getCreator();

    LocalDateTime getCreatedTime();

    LocalDateTime getStartTime();

    LocalDateTime getEndTime();

    LocalDateTime getCompletedTime();

    Integer getPriority();

    String getStatus();
}
