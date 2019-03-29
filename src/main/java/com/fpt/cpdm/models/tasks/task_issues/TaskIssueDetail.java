package com.fpt.cpdm.models.tasks.task_issues;

import java.time.LocalDateTime;

public interface TaskIssueDetail {

    Integer getId();

    String getSummary();

    String getDetail();

    String getStatus();

    LocalDateTime getCreatedTime();
}