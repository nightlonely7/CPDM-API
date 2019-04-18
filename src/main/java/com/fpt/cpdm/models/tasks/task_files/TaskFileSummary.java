package com.fpt.cpdm.models.tasks.task_files;

import com.fpt.cpdm.models.tasks.TaskSummary;
import com.fpt.cpdm.models.users.UserDisplayName;

public interface TaskFileSummary {
    TaskSummary getTask();
    Integer getId();
    UserDisplayName getCreator();
    String getFilename();
}
