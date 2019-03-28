package com.fpt.cpdm.models.tasks;

import java.time.LocalDateTime;

public interface TaskDetail extends TaskSummary {

    String getDescription();

    LocalDateTime getLastModifiedTime();
}
