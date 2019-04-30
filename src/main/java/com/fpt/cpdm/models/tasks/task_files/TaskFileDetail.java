package com.fpt.cpdm.models.tasks.task_files;

import com.fpt.cpdm.models.tasks.TaskSummary;
import com.fpt.cpdm.models.users.UserDisplayName;

import java.time.LocalDateTime;

public interface TaskFileDetail {
    Integer getId();
    TaskSummary getTask();
    String getFilename();
    String getDetailFilename();
    String getExtension();
    String getDescription();
    UserDisplayName getCreator();
    LocalDateTime getCreatedTime();
    LocalDateTime getLastModifiedTime();
}
