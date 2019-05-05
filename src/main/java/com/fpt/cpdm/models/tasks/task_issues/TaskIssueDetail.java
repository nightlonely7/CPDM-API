package com.fpt.cpdm.models.tasks.task_issues;

import com.fpt.cpdm.models.users.UserDisplayName;

import java.time.LocalDateTime;

public interface TaskIssueDetail {

    Integer getId();

    String getSummary();

    String getDescription();

    Boolean getCompleted();

    LocalDateTime getCompletedTime();

    UserDisplayName getCreator();

    LocalDateTime getCreatedTime();

    UserDisplayName getLastEditor();

    LocalDateTime getLastModifiedTime();
}
